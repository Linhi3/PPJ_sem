# Semestrální práce pro předmět PPJ

## Předpoklady

- Java 17
- Maven 4.0.0
- MySQL

## Body zadání:
 - Použit Maven pro sestavení.

 - Postaveno na Spring Boot

 - Datový model v MySQL

 - Rest API pro přímou komunikaci

 - Testy pro přidávání, zobrazování a odebírání států a měst

 - Konfigurace řešena externě v application.properties

 - Logování s výpisem do souboru (logs/ppj.log/spring.log)
   

## Komunikace:


### Státy
1. Přidávání států: PUT
     ```bash
     http://localhost:8080/states?stateIso=USA&stateName=spojené státy
     ```
   Lze přidat stát pomocí jeho jména a ISO kódu

2. Zobrazování dostupných států:  GET
   ```bash
   http://localhost:8080/states
   ```
   Zobrazí se všechny přidané státy
   
3. Mazání států: DELETE
   ```bash
   http://localhost:8080/states?stateIso=CZ
   ```
   Smaže přidaný stát pomocí jeho ISO kódu (Po smazání státu se vymažou i všechny města v něm)

### Města
4. Přidávání měst: PUT
   ```bash
   http://localhost:8080/cities?stateIso=CZ&cityName=Liberec
   ```
   Lze přidat město do dostupného státu

5. Zobrazování dostupných měst ve státu: GET
   ```bash
   http://localhost:8080/cities?stateIso=CZ
   ```
   Zobrazí se všechny přidané města ve státu
   
6. Mazání měst: DELETE
   ```bash
   http://localhost:8080/cities?stateIso=CZ&cityName=Liberec
   ```
   Smaže přidané město pomocí ISO kodu státu a jeho jména (Po smazání města se vymažou všechny data v něm)

### Zobrazování dat

7. Zobrazení dat pro město: GET
   ```bash
   http://localhost:8080/data?stateIso=CZ&cityName=Liberec
   ```
   Stáhne a zobrazí data pro určité město


