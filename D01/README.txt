In this file we will describe the decisions we have made in case of we consider that requirements are not detailed enough:

- When accepting a Request, in order to get the system to be capable of suggest a good position, is necessary that the system has stored the maximum number of rows and maximum number of columns of each procession. We have decided to store this attributes in Customisation, so that all processions have the same limit and administrators can adjust this parameters.

- In the case of a member drop out a Brotherhood an he re-enroll to the same one, it's not described in the requirements if it's necessary to have stored all enrollment periods or only the last period in the Brotherhood. We have decided to store the last periods only, because in this way we may improve the performance of some queries.

- There is a conflict between Req.-4 and Req.-17. We cannot implement both requirements at the same time. We have decided to implement Req.-4.

- Personal data of the actors have some attributes that may be sensible, so we need to protect them. In this way, we have make the following decisions:
 	- All users can display all attributes of a Brotherhood.
	- All users can display all attributes of a Member except the address and the phone number.
	- Brotherhoods can display the address and the phone number of a Member in the case of the Member is enrolled in said Brotherhood.
	- Personal data of administrators only can be displayed by the own administrator.

- We have to implement https in our system in order to obey the GDPR. However, this implementation involves modifying tomcat server configuration files (server.xml), which is outside the Acme-Madruga project. Following the "On you deliverables" document, we must provide the folder with our project, not our entire workspace. Because of that, we have decided to deliver the project without any https implementation, but a pdf document where we detail the steps in order to get https works in our system.