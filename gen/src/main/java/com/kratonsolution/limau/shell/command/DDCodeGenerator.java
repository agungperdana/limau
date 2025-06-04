package com.kratonsolution.limau.shell.command;

import com.kratonsolution.limau.shell.processor.FieldProcessor;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.AllArgsConstructor;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Agung Dodi Perdana
 * @email agung.dodi.perdana@gmail.com
 */
@AllArgsConstructor
@Command
public class DDCodeGenerator {

    private Configuration cfg;

    @Command(command = "entity")
    public String genEntity(@Option(longNames = "name", shortNames = 'n') String name,
                            @Option(longNames = "package", shortNames = 'p') String packageName,
                            @Option(shortNames = 'f', longNames = "fields", required = false, arityMax = 100) String fields)
    {

        try {

            String pckDir = toPackageFolder(packageName);
            String srcDir = String.format("%s%s%s%s%s%s", "src",File.separator, "main",File.separator, "java", File.separator);
            String apiDir = String.format("api%s%s%s", File.separator, srcDir, pckDir);
            String implDir = String.format("impl%s%s%s", File.separator, srcDir, pckDir);


            FieldProcessor fp = FieldProcessor.forField(fields);

            Map<String, Object> model = new HashMap<>();
            model.put("packageName", packageName);
            model.put("entityName", name);
            model.put("imports", fp.getImpprts());
            model.put("fields", fp.getFields());

            // let's prepare the target folder
            prepareFolder(
                    String.format("%s%s%s", apiDir, File.separator, "application"),
                    String.format("%s%s%s", implDir, File.separator, "domain"),
                    String.format("%s%s%s", implDir, File.separator, "repository")
            );

            createEntity(name, model, implDir);
            createRepository(name, model, implDir);
            createCreateCommand(name, model, apiDir);
            createUpdateCommand(name, model, apiDir);
            createDeleteCommand(name, model, apiDir);
            createApplication(name, model, apiDir);
            createQuery(name, model, apiDir);
            createData(name, model, apiDir);
        }
        catch (IOException | TemplateException e) {
            return String.format("Failed with reason: %s", e.getMessage());
        }

        return "entity successfully created.";
    }

    private void createEntity(String name, Map<String, Object> model, String location) throws IOException, TemplateException {

        Path path = Paths.get(String.format("%s%s%s%s%s.java", location, File.separator, "domain", File.separator, name));

        FileWriter fw = new FileWriter(path.toFile());
        PrintWriter print = new PrintWriter(fw);

        Template temp = cfg.getTemplate("entity.template");
        temp.process(model, print);

        print.close();
    }

    private void createRepository(String name, Map<String, Object> model, String location) throws IOException, TemplateException {

        Path path = Paths.get(String.format("%s%s%s%s%sRepository.java", location, File.separator, "repository", File.separator, name));
        FileWriter fw = new FileWriter(path.toFile());
        PrintWriter print = new PrintWriter(fw);

        Template temp = cfg.getTemplate("repository.template");
        temp.process(model, print);

        print.close();
    }

    private void createCreateCommand(String name, Map<String, Object> model, String location) throws IOException, TemplateException {

        Path path = Paths.get(String.format("%s%s%s%sCreate%sCommand.java", location, File.separator, "application", File.separator, name));
        FileWriter fw = new FileWriter(path.toFile());
        PrintWriter print = new PrintWriter(fw);

        Template temp = cfg.getTemplate("create-command.template");
        temp.process(model, print);

        print.close();
    }

    private void createUpdateCommand(String name, Map<String, Object> model, String location) throws IOException, TemplateException {

        Path path = Paths.get(String.format("%s%s%s%sUpdate%sCommand.java", location, File.separator, "application", File.separator, name));
        FileWriter fw = new FileWriter(path.toFile());
        PrintWriter print = new PrintWriter(fw);

        Template temp = cfg.getTemplate("update-command.template");
        temp.process(model, print);

        print.close();
    }

    private void createDeleteCommand(String name, Map<String, Object> model, String location) throws IOException, TemplateException {

        Path path = Paths.get(String.format("%s%s%s%sDelete%sCommand.java", location, File.separator, "application", File.separator, name));
        FileWriter fw = new FileWriter(path.toFile());
        PrintWriter print = new PrintWriter(fw);

        Template temp = cfg.getTemplate("delete-command.template");
        temp.process(model, print);

        print.close();
    }

    private void createApplication(String name, Map<String, Object> model, String location) throws IOException, TemplateException {

        Path path = Paths.get(String.format("%s%s%s%s%sApplication.java", location, File.separator, "application", File.separator, name));
        FileWriter fw = new FileWriter(path.toFile());
        PrintWriter print = new PrintWriter(fw);

        Template temp = cfg.getTemplate("application-api.template");
        temp.process(model, print);

        print.close();
    }

    private void createQuery(String name, Map<String, Object> model, String location) throws IOException, TemplateException {

        Path path = Paths.get(String.format("%s%s%s%s%sQuery.java", location, File.separator, "application", File.separator, name));
        FileWriter fw = new FileWriter(path.toFile());
        PrintWriter print = new PrintWriter(fw);

        Template temp = cfg.getTemplate("query.template");
        temp.process(model, print);

        print.close();
    }

    private void createData(String name, Map<String, Object> model, String location) throws IOException, TemplateException {

        Path path = Paths.get(String.format("%s%s%s%s%sData.java", location, File.separator, "application", File.separator, name));
        FileWriter fw = new FileWriter(path.toFile());
        PrintWriter print = new PrintWriter(fw);

        Template temp = cfg.getTemplate("domain-data.template");
        temp.process(model, print);

        print.close();
    }

    private String toPackageFolder(String packageName) {
        return packageName.replace(".", File.separator);
    }

    private void prepareFolder(String... pathName) throws IOException {

        for(String uri: pathName){
            Path path = Paths.get(uri);
            Files.createDirectories(path);

        }
    }
}
