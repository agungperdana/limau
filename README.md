I am lazy so i create this generator to help me create classess.
This code generator will create classes under 2 folder: 
- api
- impl

command:
entity -n xxx -p xxx -f xxx:yyy
-n : Entity name
-p : Package name
-f : field name: type

example:
entity -n Investment -p com.adp.inv -f code:String investor date:Instant amount:BigDecimal

will result:

Investment.java
InvestmentRepository.java
InvestmentApplication.java
CreateInvestmentCommand.java
UpdateInvestmentCommand.java
DeleteInvestmentCommand.java
InvestmentQuery.java
InvestmentData.java
