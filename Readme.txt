SETUP INSTRUCTIONS:
===========================================================================
1. Navigate to META-INF directory.
2. Update the "context.xml" file by commenting out or uncommenting a specific database resource
    - If at the University, uncomment the "At Uni Resource" and comment out the "At home resource"
    - If at home or any other network uncomment the "At home resource" and comment out the "At Uni resource" then create a Putty connection to jumpgate.newcastle.edu.au with an SSH tunnel to teachdb:3306 with port 30001 (detailed instructions can be found on blackboard).
3. Compile the application then run PATH TO TOMCAT\bin\startup.bat.
4. Setup is completed, classes are pre-compiled, database is pre-populated with data and login credientials (see below).
5. Lanuch the application by visiting one the of the below URL's found in the "URLS" section (see below).

===========================================================================


TEST LOGIN CREDENTIALS:
===========================================================================
Test login credientials have been stored inside the database for testing purposes. Below are the
login credients for both users and staff:
________________________________________________________________
|        EMAIL          |      PASSWORD       |    USER ROLE    |
|  c3237808@uon.edu.au  |      test1234       |      Staff      |
|  c3180044@uon.edu.au  |      test1234       |      Staff      |
|  c3281849@uon.edu.au  |      test1234       |      Staff      |
|  c1234567@uon.edu.au  |      test1234       |      User       |
|  c1111111@uon.edu.au  |      test1234       |      User       |
|  c2222222@uon.edu.au  |      test1234       |      User       |
|_______________________________________________________________|

===========================================================================


URLS:
===========================================================================
PC URL: http://localhost:8080/c3180044_c3281849_c3237808_FinalProject
Mobile URL: http://"Your_PC_IPv4_address":8080/c3180044_c3281849_c3237808_FinalProject

===========================================================================


ADDITIONAL REQUIREMENTS:
===========================================================================
1. (weight 10) “It would be good if the system could suggest related Knowledge-Base articles while I am
reporting my issue”

2. (weight 10) “The current system works fine, I don’t want a new system exposing my internal comments to
users if I mark it as a Knowledge-Base article” – IT staff. 

9. (weight 20) “For a Knowledge-Base to work we need to be able to capture the data relating to an incident in a more meaningful manner. Currently we capture this data in a free-form textbox (Appendix 6.1). The suggested text is related to the category of incident but can be cleared or ignored by users” – IT staff

13. (weight 5) “I would like to stay anonymous if the issue I have reported is included in the Knowledge-Base.” – User

14. (weight 5) “We should be able to view Knowledge-Base articles sorted by their categories” – User

15. (weight 5) “We should be able to sort the issues by the date that they were reported” – IT staff

22. (total weight * 2 + 5) “It would be nice if I could view the entire system on my mobile device as well” – User

===========================================================================


DATABASE SETUP:
===========================================================================
A database is pre-configured however if you'd like to setup your own the MySQL script
is located PATH TO TOMCAT\webapps\c3180044_c3281849_c3237808_FinalProject/Database/

If you have your own UoN U drive
1. Copy CreateDBScript.sql into your U drive
2. Connect to jumpgate like in the setup instructions
3. Enter in the command "mysql -h teachdb -u c3237808 -p"
4. Enter "260696" for the password
5. Enter in the command "source PATH_TO_SCRIPT_IN_U_DRIVE/CreateDBScript.sql";

Alternatively you could modify the sql script and context.xml to use another MySQL server or local database

===========================================================================


COMPILATION:
===========================================================================
1. Download and Install JDK (Recommened version 8)
2. Download Tomcat 8.5
3. Add both the following libraries to your classpath
	PATH TO TOMCAT\lib\servlet-api.jar
	PATH TO TOMCAT\lib\jsp-api.jar
4. Compile the java package located in WEB-INF/classes/

PREWRITTEN JAVAC COMMANDS
Navigate to the root directory PATH TO TOMCAT\webapps\c3180044_c3281849_c3237808_FinalProject

# UNIX
javac WEB-INF/classes/itserviceportal/*/*.java WEB-INF/classes/itserviceportal/model/*/*.java

# MS-DOS
javac WEB-INF\classes\itserviceportal\controller\*.java WEB-INF\classes\itserviceportal\model\beans\*.java WEB-INF\classes\itserviceportal\model\datalayer\*.java WEB-INF\classes\itserviceportal\customtags\*.java

===========================================================================


DIRECTORY STRUCTURE:
===========================================================================
.
├── Database                    # CreateDBScript.sql
├── Draft Documentation         # Initial documentation files
├── Final Documentation         # Revised documentation files
├── META-INF                    # context.xml
└── WEB-INF                     # web.xml, customtags.tld
    ├── classes                 #
    │   └── itserviceportal     #
    │       ├── controller      # Servlets for navigation, Filters for authentication, Listeners for session management
    │       ├── customtags      # JSP SimpleTags
    │       └── model			# Java Package
    │           ├── beans       # Beans
    │           └── datalayer   # Data Access Objects
    ├── lib                     # Jar Libraries
    └── view                    #
        ├── css                 # CSS Files
        ├── js                  # Javascript Files
        └── jsp                 # All access pages
            ├── includes        # JSPs only included/imported in other pages
            ├── staff           # Staff only pages
            ├── user            # User/Staff only pages
            └── userx           # User only pages

===========================================================================




