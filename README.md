Summary:

This program was created to parse .csv files with 10 columns correlating to A,B,C,...,J. The information is then checked for integrity,
i.e., looking for rows in the .csv file that are incomplete. These incomplete rows are stored to a new .csv named: <input-filename>-bad.csv.
The application makes the bad.csv based off of the file name of the original file that as given to the program. This program will add the
complete rows to a sqlite database stored in the memory. It records the total number of successful parses(complete rows) and failed parses(incomplete rows)
encountered. It will store the information into a log. Upon completion of parsing the information, the program will ask the user if they would
like for the contents of either the database or the bad.csv file created to be printed out to the commandline. This can be utilize to ensure
the program functioned. The user will also have the option to exit out of the program from there as well.

Steps for install:
1. Ensure that the sqlite-jdbc.jar file is apart of the build path as a library. This github resource is set up intiatly with that in mind
utilizing the eclipse IDE. If sqlite-jdbc.jar is not set up in the build path as a library, it is stored within the MS3CodingChallenge file
for convience of connecting it to the build path.
2. Build program and execute from there.
3. Program will ask for user input at certain stages. The user input section for printing out the database or bad.csv file utilizes a integer
value to determine how the process. The corresponding integer value is next to the action it is associated with and will be printed out in the command line.


I created this program with the idea for some flexibility. You can input the file name of what ever .csv file you intend to run the program on.
Based off of the instructions/requirements of the assessment this program is designed to work with only 10 column databases/csv files. I believe
that this could be expanded upon with some alteration to be a bit more flexible.

The utilizations of the files, and the databases is implemented with the intent to be dynamic. To hopefully ensure that the "paths" will work
regardless of deceive utilizing the program.

When building this program I started with the basic building blocks of being able to open and read the data from the .csv file. From there
I implemented the parsing function. Once I could properly parse the data, I transitioned to working on storing the information in their respective spots.
Storing the data onto bad.csv was where I went first, got that locked down and then progressed onward to the database creation and storing.
I implemented the database to be stored into the memory as per the direction recieved when communicating with the recruiter about the database.
The way I was able to implement this does have the database nameless, but I left a comment where it should instead store the database as a file which will have
the name for the database.

The last two steps of the build process were implementing a user interaction portion to the program. I implemented this to allow the user
to have some control over the file that would be processed. As long as the file is stored with the program upon building it should be able
to parse the file as long as it fits with the confinds of the requirements for this assessment. I also implemented functions for the program
to print out the contents of the database and the bad.csv file to the command line. This was useful to me when building to ensure the program
was functioning correctly, and I figured it might be a reasonable thing to keep in the code and provide access to. These functions could definitely
be expanded upon to allow for searching of the information as well. The log file was simple, because when I created the parsing functionality
I had the program keep track of the good and bad parses, since those were the only two possibilites for parsing I didn't require the computer
to count the total number of parses done, because that can be calculated by adding both the successful and failed parses. total success(good), total failures(bad),
and combined totals can be found in the log. The log should update with each run of the function.
