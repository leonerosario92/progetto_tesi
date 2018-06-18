
In order to execute memory measurement test is necessary to install object-explorer.jar in local maven repository.
To do so, execute the following command under the project root directory : 

		mvn install:install-file -Dfile=${project.dir}/ext-lib/object-explorer.jar -DgroupId=com.memory-measurer \
    -DartifactId=object-explorer -Dversion=1.0 -Dpackaging=jar
    

    
After the execution of a Test, a report will be appended to  test-log.txt file.