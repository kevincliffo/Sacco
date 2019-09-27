CREATE TABLE members (
  UserId int(11) NOT NULL AUTO_INCREMENT,
  MemberNo varchar(50) NOT NULL,
  FullName varchar(50) NOT NULL,
  BirthDate varchar(50) NOT NULL,
  MobileNo varchar(50) NOT NULL,
  IDNumber varchar(50) NOT NULL,
  Email varchar(50) NOT NULL,
  Address varchar(50) NOT NULL,
  PasswordHash varchar(256) NOT NULL,
  Salt varchar(256) NOT NULL,
  CreatedDate datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
