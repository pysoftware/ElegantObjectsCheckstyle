# How to use?

0) Don't forget to install ```CheckStyle-idea``` in ```File -> Settings -> Plugins```
1) ```mvn package```
2) Go to your project
3) Go to ```File -> Settings -> Tools -> Checkstyle```
4) Click ```+``` and choose ```JAR``` to ```Third-party checks```
5) Click ```+``` to add configuration file which is in ```~/ElegantObjectsCheckstype/config/elegant-objects-idea.xml```
and choose it in list

# Checkstyle for intellij idea

> For now only works checking for a multiple returns in a method

Example for testing:
```java
public int g() {
        switch (5) {
            case 2:
                if (1 > 5) {
                    switch (5) {
                        case 5:
                            return 5;
                    }
                    return 1;
                }
                return 5;
            case 4:

                return 5;
        }
        if (1 < 2) {
            if (3 > 2) {
                int b = 1;
                return 5;
            }
            return 6;
        }

        return 1;
    }
```

> NOTE! Return checking don't work with braces in switch case statement

Won't work:
```java
switch(a) {
    case 5: {
        // a lof of returns' won't be checked
    }    
}
```

# Useful links
[Checkstyle documentation](https://checkstyle.org/writingchecks.html)  
[Checkstyle github repo](https://github.com/checkstyle/checkstyle)  
[Checkstyle TokenTypes description](https://checkstyle.org/apidocs/com/puppycrawl/tools/checkstyle/api/TokenTypes.html)

# Support
> Will be glad to see your support in the development of this project (Elegant objects static analyzer for IDEA).  
> <br>
> You can write me in telegram: @lhtmc

#### First version of Elegant objects static analyzer for IDEA
[github](https://github.com/pysoftware/ElegantObjectsIntellijIdeaStaticCodeAnalyzer)