 # CreateDBScript.sql
 #
 # Creates all the database tables required by the application and inserts dummy data
 # for testing / marking purposes.
 #
 # @author Brice Purton, Jonathan Williams, Wajdi Yournes
 # @version 1.0
 # @since 19-05-2018
 

# Dropping the database and creating again to remove all data. 
DROP DATABASE c3237808_db;
CREATE DATABASE c3237808_db;
USE c3237808_db;


# Creating the Users table
CREATE TABLE tbl_User (
    UserID INT NOT NULL AUTO_INCREMENT,
    Email CHAR(19) NOT NULL,
    UserPassword VARCHAR(50) NOT NULL,
    FirstName VARCHAR(255) NOT NULL,
    LastName VARCHAR(255) NOT NULL,
    ContactNum CHAR(10) NOT NULL,
    UserRole VARCHAR(5) NOT NULL DEFAULT 'User',
    PRIMARY KEY (UserID)
);


# Creating the Category Table
CREATE TABLE tbl_Category (
    CategoryID INT NOT NULL AUTO_INCREMENT,
    CatName VARCHAR(8) NOT NULL,
    PRIMARY KEY (CategoryID)
);


# Creating the Support Ticket Table
CREATE TABLE tbl_SupportTicket (
    TicketID INT NOT NULL AUTO_INCREMENT,
    Title VARCHAR(200) NOT NULL,
    Descrip VARCHAR(20000) NOT NULL,
    TicketState VARCHAR(255) NOT NULL DEFAULT 'new',
    ReportedOn DATETIME NOT NULL,
    ResolvedOn DATETIME,
    IsKnowledgeBase TINYINT(1) NOT NULL DEFAULT 0,
    ResolutionDetails VARCHAR(20000),
    CreatedByUserID INT NOT NULL,
    ResolvedByUserID INT,
    CategoryID INT NOT NULL,

    PRIMARY KEY (TicketID),
    FOREIGN KEY (CreatedByUserID) REFERENCES tbl_User(UserID),
    FOREIGN KEY (ResolvedByUserID) REFERENCES tbl_User(UserID),
    FOREIGN KEY (CategoryID) REFERENCES tbl_Category(CategoryID)
);


# Creating the Comment Table
CREATE TABLE tbl_Comment (
    CommentID INT NOT NULL AUTO_INCREMENT,
    CommentText VARCHAR(20000) NOT NULL,
	CommentDate DATETIME NOT NULL,
    UserID INT NOT NULL,
    TicketID INT NOT NULL,

    PRIMARY KEY (CommentID), 
    FOREIGN KEY (UserID) REFERENCES tbl_User(UserID),
    FOREIGN KEY (TicketID) REFERENCES tbl_SupportTicket(TicketID)
);


# Creating the IssueDetails Table
CREATE TABLE tbl_IssueDetails (
    IssueDetailsID INT NOT NULL AUTO_INCREMENT,
    QuestionText VARCHAR(255) NOT NULL,
    ResponseText VARCHAR(20000) NOT NULL,
	TicketID INT NOT NULL,
    
    PRIMARY KEY (IssueDetailsID), 
    FOREIGN KEY (TicketID) REFERENCES tbl_SupportTicket(TicketID)
);


# Creating the Notification Table
CREATE TABLE tbl_Notification (
    NotificationID INT NOT NULL AUTO_INCREMENT,
    NotificationAction VARCHAR(20) NOT NULL,
	NotificationDate DATETIME NOT NULL,
    UserID INT NOT NULL,
    TicketID INT NOT NULL,

    PRIMARY KEY (NotificationID), 
    FOREIGN KEY (UserID) REFERENCES tbl_User (UserID),
    FOREIGN KEY (TicketID) REFERENCES tbl_SupportTicket(TicketID)
);


# Creating Database View For Support Tickets
CREATE VIEW vw_SupportTickets AS
SELECT	t.TicketID, t.Title, t.Descrip, t.TicketState, t.ReportedOn, t.ResolvedOn, t.IsKnowledgeBase, t.ResolutionDetails, t.CategoryID, cat.CatName AS CategoryName, createdBy.UserID AS CreatedByUserID, createdBy.Email AS CreatedByEmail, createdBy.FirstName AS CreatedByFName, createdBy.LastName AS CreatedByLName, createdBy.ContactNum AS CreatedByContactNum, createdBy.UserRole AS CreatedByRole, resolvedBy.UserID AS ResolvedByUserID, resolvedBy.Email AS ResolvedByEmail, resolvedBy.FirstName AS ResolvedByFName, resolvedBy.LastName AS ResolvedByLName, resolvedBy.ContactNum AS ResolvedByContactNum, resolvedBy.UserRole AS ResolvedByRole
FROM tbl_SupportTicket t
INNER JOIN tbl_Category cat ON (t.CategoryID = cat.CategoryID)
INNER JOIN tbl_User createdBy ON (t.CreatedByUserID = createdBy.UserID)
LEFT JOIN tbl_User resolvedBy ON (t.ResolvedByUserID = resolvedBy.UserID);


# Creating Database View for Comments
CREATE VIEW vw_Comments
AS
SELECT c.*, u.FirstName, u.LastName, u.Email, u.ContactNum, u.UserRole
FROM tbl_Comment c
INNER JOIN tbl_User u ON (c.UserID = u.UserID);

# END: CREATE DATABASE TABLES
# ----------------------------------------------------------------
# ----------------------------------------------------------------


# BEGIN: INSERT DUMMY TEST DATA
# ----------------------------------------------------------------
# ----------------------------------------------------------------

# Inserting Values Into the Users Table
INSERT INTO tbl_User (Email, UserPassword, FirstName, LastName, ContactNum, UserRole) 
VALUES ('c3237808@uon.edu.au', 'test1234', 'Jono', 'Williams', '0412345678', 'Staff');
INSERT INTO tbl_User (Email, UserPassword, FirstName, LastName, ContactNum, UserRole)
VALUES ('c3180044@uon.edu.au', 'test1234', 'Brice', 'Purton', '0412345678', 'Staff');
INSERT INTO tbl_User (Email, UserPassword, FirstName, LastName, ContactNum, UserRole)
VALUES ('c3281849@uon.edu.au', 'test1234', 'Wajdi', 'Younes', '0412345678', 'Staff');
INSERT INTO tbl_User (Email, UserPassword, FirstName, LastName, ContactNum, UserRole)
VALUES ('c1234567@uon.edu.au', 'test1234', 'Billy', 'Jones', '0412345678', 'User');
INSERT INTO tbl_User (Email, UserPassword, FirstName, LastName, ContactNum, UserRole)
VALUES ('c1111111@uon.edu.au', 'test1234', 'Tom', 'Scott', '0412345678', 'User');
INSERT INTO tbl_User (Email, UserPassword, FirstName, LastName, ContactNum, UserRole)
VALUES ('c2222222@uon.edu.au', 'test1234', 'Joe', 'Blogs', '0478945612', 'User');

# Inserting Values into the Category table
INSERT INTO tbl_Category (CatName) VALUES ('Network');
INSERT INTO tbl_Category (CatName) VALUES ('Software');
INSERT INTO tbl_Category (CatName) VALUES ('Hardware');
INSERT INTO tbl_Category (CatName) VALUES ('Email');
INSERT INTO tbl_Category (CatName) VALUES ('Account');




# Inserting into tickets tables
INSERT INTO tbl_SupportTicket (Title, Descrip, TicketState, ReportedOn, ResolvedOn, IsKnowledgeBase, ResolutionDetails, CreatedByUserID, ResolvedByUserID, CategoryID)
VALUES ('No internet connection', 'Having trouble connecting to google.', 'new', '2018-06-07 16:34:00', NULL, 0, NULL, 6, NULL, 1);

INSERT INTO tbl_SupportTicket (Title, Descrip, TicketState, ReportedOn, ResolvedOn, IsKnowledgeBase, ResolutionDetails, CreatedByUserID, ResolvedByUserID, CategoryID)
VALUES ('Adobe Photoshop will not load', 'The application does not open when clicked.', 'in progress', '2018-04-15 08:48:00', NULL, 0, NULL, 6, NULL, 2);

INSERT INTO tbl_SupportTicket (Title, Descrip, TicketState, ReportedOn, ResolvedOn, IsKnowledgeBase, ResolutionDetails, CreatedByUserID, ResolvedByUserID, CategoryID)
VALUES ('Computer very slow', 'Computer in group study room is very slow', 'in progress', '2018-05-21 10:57:00', NULL, 0, NULL, 4, NULL, 3);

INSERT INTO tbl_SupportTicket (Title, Descrip, TicketState, ReportedOn, ResolvedOn, IsKnowledgeBase, ResolutionDetails, CreatedByUserID, ResolvedByUserID, CategoryID)
VALUES ('Can not send a email', 'email is failing', 'completed', '2018-06-09 10:39:00', '2018-06-09 12:22:00', 0, 'Reset incoming and outgoing mail settings.', 6, 1, 4);

INSERT INTO tbl_SupportTicket (Title, Descrip, TicketState, ReportedOn, ResolvedOn, IsKnowledgeBase, ResolutionDetails, CreatedByUserID, ResolvedByUserID, CategoryID)
VALUES ('My account has been locked.', 'Cannot access my account or login.', 'resolved', '2018-05-27 15:04:36', '2018-06-01 11:13:36', 1, 'Reactivated account in active directory.', 5, 2, 5);

INSERT INTO tbl_SupportTicket (Title, Descrip, TicketState, ReportedOn, ResolvedOn, IsKnowledgeBase, ResolutionDetails, CreatedByUserID, ResolvedByUserID, CategoryID)
VALUES ('Slow internet', 'The internet is super slow.', 'resolved', '2018-04-18 09:45:36', '2018-04-19 17:09:36', 1, 'Reset some network configurations.', 4, 3, 1);

INSERT INTO tbl_SupportTicket (Title, Descrip, TicketState, ReportedOn, ResolvedOn, IsKnowledgeBase, ResolutionDetails, CreatedByUserID, ResolvedByUserID, CategoryID)
VALUES ('Can''t connect to wifi', 'My phone cannot connect to the wifi on campus from any location.', 'new', '2018-05-20 10:06:07', NULL, 0, NULL, 5, NULL, 1);

INSERT INTO tbl_SupportTicket (Title, Descrip, TicketState, ReportedOn, ResolvedOn, IsKnowledgeBase, ResolutionDetails, CreatedByUserID, ResolvedByUserID, CategoryID)
VALUES ('Can\'t Print', 'I can\'t print using a printer in the Auchmuty library','resolved','2018-05-21 07:55:42','2018-05-22 21:43:08',0,'Please try another printer while we get a technician to look at the printer.', 5, 1, 3);

INSERT INTO tbl_SupportTicket (Title, Descrip, TicketState, ReportedOn, ResolvedOn, IsKnowledgeBase, ResolutionDetails, CreatedByUserID, ResolvedByUserID, CategoryID)
VALUES ('Connect to U drive from home', 'How do I connect to my U drive from home?', 'completed', '2018-06-02 11:08:42', '2018-06-08 14:26:08', 1, '1. Download filezilla or a similar FTP client.\r\n2. At the top enter the following\r\n\ta. Host: sftp://jumpgate.newcastle.edu.au\r\n\tb. User: your university username\r\n\tc. Password: your university password\r\n\td. Port: leave blank\r\n3. This should connect to the U Drive and now you can put in your sql files to run in putty.', 4, 2, 1);

INSERT INTO tbl_SupportTicket (Title, Descrip, TicketState, ReportedOn, ResolvedOn, IsKnowledgeBase, ResolutionDetails, CreatedByUserID, ResolvedByUserID, CategoryID)
VALUES ('Computer won\'t power on', 'I\'m trying to use a uni computer and it won\' turn on','in progress','2018-05-07 17:01:52', NULL, 0, NULL, 6, NULL, 3);

# Inserting into comments table
INSERT INTO tbl_Comment (CommentText, UserID, TicketID, CommentDate)
VALUES ('Still wont open.', 6, 2, '2018-04-17 09:22:00');
INSERT INTO tbl_Comment (CommentText, UserID, TicketID, CommentDate)
VALUES ('Reinstalled the application. Please try again.', 1, 2, '2018-04-17 11:57:00');

INSERT INTO tbl_Comment (CommentText, UserID, TicketID, CommentDate)
VALUES ('Thank you, it works now.', 6, 3, '2018-05-21 12:07:00');
INSERT INTO tbl_Comment (CommentText, UserID, TicketID, CommentDate)
VALUES ('No problem.', 2, 3, '2018-05-21 19:36:00');

INSERT INTO tbl_Comment (CommentText, UserID, TicketID, CommentDate)
VALUES ('Has this been fixed yet?', 6, 4, '2018-06-08 14:41:00');
INSERT INTO tbl_Comment (CommentText, UserID, TicketID, CommentDate)
VALUES ('Yes. Try again now.', 3, 4, '2018-06-08 14:48:00');

INSERT INTO tbl_Comment (CommentText, UserID, TicketID, CommentDate)
VALUES ('I cant think of a comment', 6, 5, '2018-05-28 11:11:07');
INSERT INTO tbl_Comment (CommentText, UserID, TicketID, CommentDate)
VALUES ('This is a another comment', 3, 5, '2018-05-28 21:38:21');
INSERT INTO tbl_Comment (CommentText, UserID, TicketID, CommentDate)
VALUES ('This is a another another comment', 3, 5, '2018-05-29 01:01:36');
INSERT INTO tbl_Comment (CommentText, UserID, TicketID, CommentDate)
VALUES ('What am I still doing awake at this time', 3, 5, '2018-05-29 03:56:15');

INSERT INTO tbl_Comment (CommentText, UserID, TicketID, CommentDate)
VALUES ('My internet is horrible atm.', 4, 6, '2018-04-18 10:19:08');
INSERT INTO tbl_Comment (CommentText, UserID, TicketID, CommentDate)
VALUES ('This issue has been resolved for you. Have a good day', 2, 6, '2018-04-19 11:21:11');
INSERT INTO tbl_Comment (CommentText, UserID, TicketID, CommentDate)
VALUES ('Thanks!', 4, 6, '2018-04-18 16:45:44');




# Inserting into the issue details table
# Ticket 1 issue details: Network Problem
INSERT INTO tbl_IssueDetails (QuestionText, ResponseText, TicketID)
VALUES ("Device", "Uni computer", 1);
INSERT INTO tbl_IssueDetails (QuestionText, ResponseText, TicketID)
VALUES ("Location", "ES205", 1);
INSERT INTO tbl_IssueDetails (QuestionText, ResponseText, TicketID)
VALUES ("Browser", "Google Chrome", 1);
INSERT INTO tbl_IssueDetails (QuestionText, ResponseText, TicketID)
VALUES ("Website you're trying to connect to?", "www.google.com.au", 1);
INSERT INTO tbl_IssueDetails (QuestionText, ResponseText, TicketID)
VALUES ("Are you able to access internal websites?", "No", 1);
INSERT INTO tbl_IssueDetails (QuestionText, ResponseText, TicketID)
VALUES ("Have you tried using an alternate browser?", "Yes", 1);
INSERT INTO tbl_IssueDetails (QuestionText, ResponseText, TicketID)
VALUES ("Have you tried restarting your device?", "Yes", 1);
INSERT INTO tbl_IssueDetails (QuestionText, ResponseText, TicketID)
VALUES ("Can you access the website on another device?", "Yes", 1);



# Ticket 2 issue details: Software problem
INSERT INTO tbl_IssueDetails (QuestionText, ResponseText, TicketID)
VALUES ("Device?", "Uni computer", 2);
INSERT INTO tbl_IssueDetails (QuestionText, ResponseText, TicketID)
VALUES ("Software I'm trying to use?", "Adobe Photoshop", 2);
INSERT INTO tbl_IssueDetails (QuestionText, ResponseText, TicketID)
VALUES ("Software version I'm trying to use?", "Adobe CC", 2);
INSERT INTO tbl_IssueDetails (QuestionText, ResponseText, TicketID)
VALUES ("Can you run the software?", "No", 2);
INSERT INTO tbl_IssueDetails (QuestionText, ResponseText, TicketID)
VALUES ("Have you tried running the software on another device?", "No", 2);


# Ticket 3 issue details: Hardware problem
INSERT INTO tbl_IssueDetails (QuestionText, ResponseText, TicketID)
VALUES ("Device you're trying to use?", "Uni computer", 3);
INSERT INTO tbl_IssueDetails (QuestionText, ResponseText, TicketID)
VALUES ("Location?", "Huxley Library", 3);
INSERT INTO tbl_IssueDetails (QuestionText, ResponseText, TicketID)
VALUES ("Can you access the device with your account login?", "Yes", 3);
INSERT INTO tbl_IssueDetails (QuestionText, ResponseText, TicketID)
VALUES ("Is the device damaged?", "No", 3);
INSERT INTO tbl_IssueDetails (QuestionText, ResponseText, TicketID)
VALUES ("Does the device power on?", "Yes", 3);


# Ticket 4 issue details: Email problem
INSERT INTO tbl_IssueDetails (QuestionText, ResponseText, TicketID)
VALUES ("Have you setup your email?", "Yes", 4);
INSERT INTO tbl_IssueDetails (QuestionText, ResponseText, TicketID)
VALUES ("Can you sign in?", "Yes", 4);
INSERT INTO tbl_IssueDetails (QuestionText, ResponseText, TicketID)
VALUES ("Have you tried resetting your password?", "No", 4);
INSERT INTO tbl_IssueDetails (QuestionText, ResponseText, TicketID)
VALUES ("Can you send and receive emails?", "No", 4);
INSERT INTO tbl_IssueDetails (QuestionText, ResponseText, TicketID)
VALUES ("Have you confirmed your internet connection?", "Yes", 4);


# Ticket 5 issue details: Account problem
INSERT INTO tbl_IssueDetails (QuestionText, ResponseText, TicketID)
VALUES ("Have you activated your account?", "Yes", 5);
INSERT INTO tbl_IssueDetails (QuestionText, ResponseText, TicketID)
VALUES ("Can you log into a university computer?", "No", 5);
INSERT INTO tbl_IssueDetails (QuestionText, ResponseText, TicketID)
VALUES ("Have you tried resetting your password?", "Yes", 5);
INSERT INTO tbl_IssueDetails (QuestionText, ResponseText, TicketID)
VALUES ("Error message if displayed?", "Your account has been locked. Please contact the system administrator.", 5);



# Ticket 6 issue details: Network Problem
INSERT INTO tbl_IssueDetails (QuestionText, ResponseText, TicketID)
VALUES ("Device", "Dell XPS15 personal laptop", 6);
INSERT INTO tbl_IssueDetails (QuestionText, ResponseText, TicketID)
VALUES ("Location", "Auchmuty Library", 6);
INSERT INTO tbl_IssueDetails (QuestionText, ResponseText, TicketID)
VALUES ("Browser", "Firefox", 6);
INSERT INTO tbl_IssueDetails (QuestionText, ResponseText, TicketID)
VALUES ("Website you're trying to connect to?", "Blackboard", 6);
INSERT INTO tbl_IssueDetails (QuestionText, ResponseText, TicketID)
VALUES ("Are you able to access internal websites?", "Yes", 6);
INSERT INTO tbl_IssueDetails (QuestionText, ResponseText, TicketID)
VALUES ("Have you tried using an alternate browser?", "Yes", 6);
INSERT INTO tbl_IssueDetails (QuestionText, ResponseText, TicketID)
VALUES ("Have you tried restarting your device?", "Yes", 6);
INSERT INTO tbl_IssueDetails (QuestionText, ResponseText, TicketID)
VALUES ("Can you access the website on another device?", "Yes", 6);


# Ticket 7 issue details: Network Problem
INSERT INTO tbl_IssueDetails (QuestionText, ResponseText, TicketID)
VALUES ("Device", "Google Pixel 2", 7);
INSERT INTO tbl_IssueDetails (QuestionText, ResponseText, TicketID)
VALUES ("Location", "All around campus", 7);
INSERT INTO tbl_IssueDetails (QuestionText, ResponseText, TicketID)
VALUES ("Browser", "Google Chrome", 7);
INSERT INTO tbl_IssueDetails (QuestionText, ResponseText, TicketID)
VALUES ("Website you're trying to connect to?", "Any website.", 7);
INSERT INTO tbl_IssueDetails (QuestionText, ResponseText, TicketID)
VALUES ("Are you able to access internal websites?", "No", 7);
INSERT INTO tbl_IssueDetails (QuestionText, ResponseText, TicketID)
VALUES ("Have you tried using an alternate browser?", "Yes", 7);
INSERT INTO tbl_IssueDetails (QuestionText, ResponseText, TicketID)
VALUES ("Have you tried restarting your device?", "Yes", 7);
INSERT INTO tbl_IssueDetails (QuestionText, ResponseText, TicketID)
VALUES ("Can you access the website on another device?", "Yes", 7);


# Ticket 8 issue details: Hardware problem
INSERT INTO tbl_IssueDetails (QuestionText, ResponseText, TicketID)
VALUES ("Device you're trying to use?", "Uni printer", 8);
INSERT INTO tbl_IssueDetails (QuestionText, ResponseText, TicketID)
VALUES ("Location?", "Auchmuty Library", 8);
INSERT INTO tbl_IssueDetails (QuestionText, ResponseText, TicketID)
VALUES ("Is the device damaged?", "No", 8);
INSERT INTO tbl_IssueDetails (QuestionText, ResponseText, TicketID)
VALUES ("Does the device power on?", "Yes", 8);
INSERT INTO tbl_IssueDetails (QuestionText, ResponseText, TicketID)
VALUES ("Error message if displayed?", "ERROR lolXD1235 - Document failed to print due to internal failure.", 8);


# Inserting into the notifications table for demo notifications
INSERT INTO tbl_Notification (NotificationAction, NotificationDate, UserID, TicketID)
VALUES ('startWork', NOW(), 5, 8);

INSERT INTO tbl_Notification (NotificationAction, NotificationDate, UserID, TicketID)
VALUES ('submitSolution', '2018-04-26', 6, 4);

INSERT INTO tbl_Notification (NotificationAction, NotificationDate, UserID, TicketID)
VALUES ('addKnowledge', NOW(), 5, 5);

INSERT INTO tbl_Notification (NotificationAction, NotificationDate, UserID, TicketID)
VALUES ('removeKnowledge', '2018-05-18', 4, 6);

INSERT INTO tbl_Notification (NotificationAction, NotificationDate, UserID, TicketID)
VALUES ('comment', '2018-05-18', 4, 3);

INSERT INTO tbl_Notification (NotificationAction, NotificationDate, UserID, TicketID)
VALUES ('comment', '2018-04-22', 5, 5);

INSERT INTO tbl_Notification (NotificationAction, NotificationDate, UserID, TicketID)
VALUES ('startWork', '2018-04-23', 5, 5);


# END: INSERT DUMMY TEST DATA
# ----------------------------------------------------------------
# ----------------------------------------------------------------