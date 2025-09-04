<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Thanks for Joining</title>
    <link rel="stylesheet" href="styles/main.css" type="text/css"/>
</head>
<body>
    <h1>Thanks for Joining</h1>

    <p><strong>Email:</strong> ${user.email}</p>
    <p><strong>First Name:</strong> ${user.firstName}</p>
    <p><strong>Last Name:</strong> ${user.lastName}</p>
    <p><strong>Date of Birth:</strong> ${user.dob}</p>
    <p><strong>Heard About Us:</strong> ${user.source}</p>
    <p><strong>Wants Offers:</strong> ${user.offers == 'yes' ? 'Yes' : 'No'}</p>
    <p><strong>Email Announcements:</strong> ${user.emailAnnouncements == 'yes' ? 'Yes' : 'No'}</p>
    <p><strong>Preferred Contact Method:</strong> ${user.contactMethod}</p>

    <p><a href="join.jsp">Return to Join page</a></p>
</body>
</html>