package com.kratonsolution.limau.shell.processor;

import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

/**
 * @author Agung Dodi Perdana
 * @email agung.dodi.perdana@gmail.com
 */
public class FieldProcessor {

    private final List<String[]> fields = new ArrayList<>();

    private final Map<String, String> types = new HashMap<>();

    private FieldProcessor(String fields) {

        types.put(String.class.getSimpleName(), String.class.getName());
        types.put(BigDecimal.class.getSimpleName(), BigDecimal.class.getName());
        types.put(Instant.class.getSimpleName(), Instant.class.getName());
        types.put(Long.class.getSimpleName(), Long.class.getName());
        types.put(List.class.getSimpleName(), List.class.getName());
        types.put(Map.class.getSimpleName(), Map.class.getName());
        types.put(Set.class.getSimpleName(), Set.class.getName());

        if(StringUtils.hasLength(fields)) {

            for(String field: fields.split(",")) {
                this.fields.add(field.split(":"));
            }
        }
    }

    public static FieldProcessor forField(String fields) {

        return new FieldProcessor(fields);
    }

    public void registerCustomType(String name, String fqcn) {

        if(StringUtils.hasLength(name) && StringUtils.hasLength(fqcn)) {
            this.types.put(name, fqcn);
        }
    }

    public List<String> getImpprts() {

        List<String> imports = new ArrayList<>();

        this.fields.forEach(f -> {

            String name = String.format("import %s", f.length == 1? String.class.getName(): types.get(f[1]));
            if(!imports.contains(name)) {
                imports.add(name);
            }
        });

        return imports;
    }

    public List<String[]> getFields() {

        List<String[]> _fields = new ArrayList<>();

        this.fields.forEach(f -> {

            String[] fn = new String[2];
            fn[0] = f.length == 1? String.class.getSimpleName(): f[1];
            fn[1] = f[0];

            _fields.add(fn);
        });

        return _fields;
    }

}
