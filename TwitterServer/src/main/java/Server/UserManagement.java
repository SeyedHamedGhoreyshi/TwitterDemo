package Server;

import javax.sql.rowset.serial.SerialBlob;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserManagement {

    private static boolean checkEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private static int search(String userName) {
        Connection connection = null;
        int userId = -1;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://root:123456789@localhost:3306/twitter");
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM user");
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                if (userName.equals(result.getString(14))) {
                    userId = result.getInt(15);

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userId;
    }

    public static String signUp(String userFname, String userLname, String userId, String userPass, String userPassRepeat, String userPhone, String userCountry, String userBirthday, String userEmail) throws IOException, SQLException {
        User USER = new User();
        Connection connection = null;
        FileInputStream avatarFile = new FileInputStream("E:\\TwitterServer\\defaultAvatar.jpg");
        byte[] avatarInBytes = avatarFile.readAllBytes();
        Blob avatarAsBlob = new SerialBlob(avatarInBytes);
        avatarFile.close();

        FileInputStream headerFile = new FileInputStream("E:\\TwitterServer\\defaultHeader.jpg");
        byte[] headerInBytes = headerFile.readAllBytes();
        Blob headerAsBlob = new SerialBlob(headerInBytes);
        headerFile.close();


        if (!userPass.equals(userPassRepeat)) {
            return "Passwords are not the same";
        }
        if (!checkEmail(userEmail)) {
            return "Email format is wrong";
        }
        try {
            connection = DriverManager.getConnection("jdbc:mysql://root:123456789@localhost:3306/twitter");
            PreparedStatement statement = connection.prepareStatement("INSERT into user ( USER_PASS , first_name , last_name , email , TELEFON  , BIRTHDAY , TARIKH_SABTNAME , USER_ID ,Location ,AVATAR , HEDER) values (?,?,? , ? , ? , ? , ? , ? , ? , ? ,? ) ");

            statement.setString(1, userPass);
            statement.setString(2, userFname);
            statement.setString(3, userLname);
            statement.setString(4, userEmail);
            statement.setString(5, userPhone);
            statement.setString(6, userBirthday);
            statement.setString(7, LocalDateTime.now().toString());
            statement.setString(8, userId);
            statement.setString(9, userCountry);
            statement.setBlob(10, avatarAsBlob);
            statement.setBlob(11, headerAsBlob);


            statement.executeUpdate();
            return "Ok your account created";

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                if (e.getMessage().contains("userId")) {
                    return "userId has already reserved";

                } else if (e.getMessage().contains("userEmail")) {
                    return "userEmail has already reserved";

                } else if (e.getMessage().contains("userPhone")) {
                    return "userPhone has already reserved";

                } else e.printStackTrace();
            }
        }
        return "nothing";
    }

    public static String logIn(String userId, String password) {
        Connection connection = null;
        boolean isValidUser = false;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://root:123456789@localhost:3306/twitter");
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM user");
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                if (userId.equals(result.getString(14)) && password.equals(result.getString(1))) {
                    isValidUser = true;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (!isValidUser) {
            return "fail";
        } else {
            return "success";
        }
    }

    public static String setBio(String bio, String userName) {
        int userId = search(userName);

        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://root:123456789@localhost:3306/twitter");
            PreparedStatement statement = connection.prepareStatement("update user set BIO = ? where ID = ?");
            statement.setString(1, bio);
            statement.setInt(2, userId);
            statement.executeUpdate();


//            statement = connection.prepareStatement("update user set website = ? where ID = ?");
//            statement.setString(1, bio.getWebSite());
//            statement.setInt(2, userId);
//            statement.executeUpdate();
            return "success";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "fail";
    }

    public static String follow(String userName, String followName) {
        int idFollwed = search(userName);
        int idFollower = search(followName);

        Connection connection = null;
        try {
            int vaziat = 1;
            connection = DriverManager.getConnection("jdbc:mysql://root:123456789@localhost:3306/twitter");
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM follow");
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                if (idFollwed == result.getInt(1) && idFollower == result.getInt(2)) {
                    vaziat = 0;
                }
            }
            if (vaziat == 1) {
                statement = connection.prepareStatement("INSERT into follow ( followed , follower ) values (? , ?) ");
                statement.setString(1, String.valueOf(idFollwed));
                statement.setString(2, String.valueOf(idFollower));
                statement.executeUpdate();
                statement = connection.prepareStatement("SELECT * FROM follow");
                result = statement.executeQuery();
                int followed = 0;
                while (result.next()) {
                    if (idFollwed == result.getInt(1)) {
                        followed++;
                    }
                }
                statement = connection.prepareStatement("SELECT * FROM follow");
                ResultSet result1 = statement.executeQuery();
                int following = 0;
                while (result1.next()) {
                    if (idFollower == result1.getInt(2)) {
                        following++;
                    }
                }
                statement = connection.prepareStatement("update user set numFollow = ? where ID = ?");
                statement.setInt(1, followed);
                statement.setInt(2, idFollwed);
                statement.executeUpdate();
                statement = connection.prepareStatement("update user set numFollowing = ? where ID = ?");
                statement.setInt(1, following);
                statement.setInt(2, idFollower);
                statement.executeUpdate();
                return "success";
            }
        } catch (SQLException e) {
            return "fail";
        }
        return "fail";

    }

    public static String unFollow(String userName, String followName) {
        Connection connection = null;
        int idFollwed = search(userName);
        int idFollower = search(followName);
        try {
            connection = DriverManager.getConnection("jdbc:mysql://root:123456789@localhost:3306/twitter");
            PreparedStatement statement = connection.prepareStatement("DELETE from follow  where followed = ?  AND  follower = ?");
            statement.setString(1, String.valueOf(idFollwed));
            statement.setString(2, String.valueOf(idFollower));
            statement.executeUpdate();
            statement = connection.prepareStatement("SELECT * FROM follow");
            ResultSet result = statement.executeQuery();
            int followed = 0;
            while (result.next()) {
                if (idFollwed == result.getInt(1)) {
                    followed++;
                }
            }
            statement = connection.prepareStatement("SELECT * FROM follow");
            ResultSet result1 = statement.executeQuery();
            int following = 0;
            while (result1.next()) {
                if (idFollower == result1.getInt(2)) {
                    following++;
                }
            }
            statement = connection.prepareStatement("update user set numFollow = ? where ID = ?");
            statement.setInt(1, followed);
            statement.setInt(2, idFollwed);
            statement.executeUpdate();
            statement = connection.prepareStatement("update user set numFollowing = ? where ID = ?");
            statement.setInt(1, following);
            statement.setInt(2, idFollower);
            statement.executeUpdate();
            return "success";
        } catch (SQLException e) {
            e.printStackTrace();
            return "fail";
        }
    }

    public static boolean followCheck(String userName, String followName) {
        int idFollwed = search(userName);
        int idFollower = search(followName);

        Connection connection = null;
        try {
            int vaziat = 1;
            connection = DriverManager.getConnection("jdbc:mysql://root:123456789@localhost:3306/twitter");
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM follow");
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                if (idFollwed == result.getInt(1) && idFollower == result.getInt(2)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String tweet(String userName, String text, String avatar) throws SQLException {

        try {
            String ImageString1 = avatar;
            byte[] ImageByte = Base64.getDecoder().decode(ImageString1);
            Blob ImageBlob = new SerialBlob(ImageByte);
            Connection connection = null;
            int userId = search(userName);
            connection = DriverManager.getConnection("jdbc:mysql://root:123456789@localhost:3306/twitter");
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM user");
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                if (userId == result.getInt(15)) {
                    userName = result.getString(14);
                }
            }
            statement = connection.prepareStatement("INSERT into tweet ( text  , tarikh  , idUser , pasTime , UserName , numComment , numLike , numRetweet ,AVATAR) values ( ? ,? , ? , ? , ? , ? ,? ,? , ?) ");
            statement.setString(1, text);
            statement.setString(2, LocalDateTime.now().toString());
            statement.setString(3, String.valueOf(userId));
            statement.setString(4, "1");
            statement.setString(5, userName);
            statement.setString(6, String.valueOf(0));
            statement.setString(7, String.valueOf(0));
            statement.setString(8, String.valueOf(0));
            statement.setBlob(9, ImageBlob);
            statement.executeUpdate();

            return "success";
            //statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "fail";
    }

    public static String reTweet(String userName, int retweetId, String avatar) throws SQLException {

        Connection connection = null;
        int userId = search(userName);
        String ImageString1 = avatar;
        byte[] ImageByte = Base64.getDecoder().decode(ImageString1);
        Blob ImageBlob = new SerialBlob(ImageByte);

        try {
            connection = DriverManager.getConnection("jdbc:mysql://root:123456789@localhost:3306/twitter");
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM user");
            ResultSet result = statement.executeQuery();

//                  statement.executeUpdate();
            statement = connection.prepareStatement("SELECT numRetweet FROM tweet where tweetId = ?");
            statement.setInt(1, retweetId);
            result = statement.executeQuery();
            int a = 0;
            if (result.next()) {
                a = result.getInt(1);
                a++;
            }
            //statement.executeUpdate();
            statement = connection.prepareStatement("update tweet set numRetweet = ? where tweetId = ?");
            statement.setString(1, String.valueOf(a));
            statement.setString(2, String.valueOf(retweetId));
            statement.executeUpdate();
            PreparedStatement statement1 = connection.prepareStatement("SELECT * FROM tweet where tweetId = ?");
            statement1.setString(1, String.valueOf(retweetId));
            ResultSet result1 = statement1.executeQuery();
            statement = connection.prepareStatement("INSERT into tweet ( text  , tarikh  , idUser , pasTime , UserName , numComment , numLike , numRetweet , idRetweetedTweet , idUserRetweeted , usernameUserRetweeted , AVATAR) values (? , ? , ? , ? , ? , ? , ? , ? ,? ,? , ? , ?) ");
            if (result1.next()) {
                statement.setString(1, result1.getString(1));
                statement.setString(2, result1.getString(5));
                statement.setString(3, String.valueOf(userId));
                statement.setString(4, "1");
                statement.setString(5, userName);
                statement.setString(6, String.valueOf(result1.getInt(4)));
                statement.setString(7, String.valueOf(result1.getInt(2)));
                statement.setString(8, String.valueOf(result1.getInt(3)));
                statement.setString(9, String.valueOf(retweetId));
                statement.setString(10, String.valueOf(result1.getInt(6)));
                statement.setString(11, String.valueOf(result1.getString(9)));
                statement.setBlob(12, ImageBlob);
                statement.executeUpdate();
                return "success";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "fail";
    }

    public static String Quote(String userName, int QuoteTweetId, String text, String avatar) throws SQLException {
        Connection connection = null;
        int userId = search(userName);
        String ImageString1 = avatar;
        byte[] ImageByte = Base64.getDecoder().decode(ImageString1);
        Blob ImageBlob = new SerialBlob(ImageByte);


        try {
            connection = DriverManager.getConnection("jdbc:mysql://root:123456789@localhost:3306/twitter");
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM user");
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                if (userId == result.getInt(15)) {
                    userName = result.getString(14);
                }
            }
//                  statement.executeUpdate();
            statement = connection.prepareStatement("SELECT numRetweet FROM tweet where tweetId = ?");
            statement.setInt(1, QuoteTweetId);
            result = statement.executeQuery();
            int a = 0;
            if (result.next()) {
                a = result.getInt(1);
                a++;
            }
            //statement.executeUpdate();
            statement = connection.prepareStatement("update tweet set numRetweet = ? where tweetId = ?");
            statement.setString(1, String.valueOf(a));
            statement.setString(2, String.valueOf(QuoteTweetId));
            statement.executeUpdate();
            PreparedStatement statement1 = connection.prepareStatement("SELECT * FROM tweet where tweetId = ?");
            statement1.setString(1, String.valueOf(QuoteTweetId));
            ResultSet result1 = statement1.executeQuery();
            statement = connection.prepareStatement("INSERT into tweet ( text  , tarikh  , idUser , pasTime , UserName , numComment , numLike , numRetweet ,idQuotedTweet ,AVATAR) values ( ?, ? , ? , ? , ? , ? ,? ,? , ? , ?) ");
            if (result1.next()) {
                statement.setString(1, text);
                statement.setString(2, LocalDateTime.now().toString());
                statement.setString(3, String.valueOf(userId));
                statement.setString(4, "1");
                statement.setString(5, userName);
                statement.setInt(6, 0);
                statement.setInt(7, 0);
                statement.setInt(8, 0);
                statement.setString(9, String.valueOf(QuoteTweetId));
                statement.setBlob(10, ImageBlob);
                statement.executeUpdate();
                return "success";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "unsuccess";
    }

    public static String Replies(String userName, String commentText, String avatar, int tweetId) throws SQLException {
        Connection connection = null;
        String ImageString1 = avatar;
        byte[] ImageByte = Base64.getDecoder().decode(ImageString1);
        Blob ImageBlob = new SerialBlob(ImageByte);
        try {
            connection = DriverManager.getConnection("jdbc:mysql://root:123456789@localhost:3306/twitter");
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM user");
            ResultSet result = statement.executeQuery();
            statement = connection.prepareStatement("INSERT into comment ( matn  , tarikhEnteshar   , UserName  , idTweet , prfile) values ( ? , ? , ? ,? , ? ) ");
            statement.setString(1, commentText);
            statement.setString(2, LocalDateTime.now().toString());
            statement.setString(3, userName);
            statement.setInt(4, tweetId);
            statement.setBlob(5, ImageBlob);
            statement.executeUpdate();
            statement = connection.prepareStatement("SELECT numComment FROM tweet where tweetId = ?");
            statement.setInt(1, tweetId);
            result = statement.executeQuery();
            int a = 0;
            if (result.next()) {
                a = result.getInt(1);
                a++;
            }
            //statement.executeUpdate();
            statement = connection.prepareStatement("update tweet set numComment = ? where tweetId = ?");
            statement.setString(1, String.valueOf(a));
            statement.setString(2, String.valueOf(tweetId));
            statement.executeUpdate();
            return "success";
        } catch (SQLException e) {
            e.printStackTrace();
            return "fail";
        }


    }

    public static String blocking(String blockerName, String blockedName) {
        Connection connection = null;
        int idBlocker = search(blockerName);
        int idBlocked = search(blockedName);
        try {
            connection = DriverManager.getConnection("jdbc:mysql://root:123456789@localhost:3306/twitter");
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM follow");
            ResultSet result = statement.executeQuery();
            statement = connection.prepareStatement("INSERT into Block ( blockedId , blockerId ) values (? , ?) ");
            statement.setInt(1, idBlocked);
            statement.setInt(2, idBlocker);
            statement.executeUpdate();
            statement = connection.prepareStatement("DELETE from follow  where followed = ?  AND  follower = ?");
            statement.setString(1, String.valueOf(idBlocker));
            statement.setString(2, String.valueOf(idBlocked));
            statement.executeUpdate();
            statement = connection.prepareStatement("DELETE from follow  where followed = ?  AND  follower = ?");
            statement.setString(1, String.valueOf(idBlocked));
            statement.setString(2, String.valueOf(idBlocker));
            statement.executeUpdate();
            statement = connection.prepareStatement("SELECT * FROM follow");
            result = statement.executeQuery();
            int followed = 0;
            while (result.next()) {
                if (idBlocker == result.getInt(1)) {
                    followed++;
                }
            }
            statement = connection.prepareStatement("SELECT * FROM follow");
            ResultSet result1 = statement.executeQuery();
            int following = 0;
            while (result1.next()) {
                if (idBlocker == result1.getInt(2)) {
                    following++;
                }
            }
            statement = connection.prepareStatement("update user set numFollow = ? where ID = ?");
            statement.setInt(1, followed);
            statement.setInt(2, idBlocker);
            statement.executeUpdate();
            statement = connection.prepareStatement("update user set numFollowing = ? where ID = ?");
            statement.setInt(1, following);
            statement.setInt(2, idBlocker);
            statement.executeUpdate();
            followed = 0;
            statement = connection.prepareStatement("SELECT * FROM follow");
            result = statement.executeQuery();
            while (result.next()) {
                if (idBlocked == result.getInt(1)) {
                    followed++;
                }
            }
            statement = connection.prepareStatement("SELECT * FROM follow");
            result1 = statement.executeQuery();
            following = 0;
            while (result1.next()) {
                if (idBlocked == result1.getInt(2)) {
                    following++;
                }
            }
            statement = connection.prepareStatement("update user set numFollow = ? where ID = ?");
            statement.setInt(1, followed);
            statement.setInt(2, idBlocked);
            statement.executeUpdate();
            statement = connection.prepareStatement("update user set numFollowing = ? where ID = ?");
            statement.setInt(1, following);
            statement.setInt(2, idBlocked);
            statement.executeUpdate();
            return "success";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "fail";
    }

    public static String unBlocking(String unBlocker, String unBlocked) {
        Connection connection = null;
        int idBlocker = search(unBlocker);
        int idBlocked = search(unBlocked);
        try {
            connection = DriverManager.getConnection("jdbc:mysql://root:123456789@localhost:3306/twitter");
            PreparedStatement statement = connection.prepareStatement("DELETE from block  where blockedId = ?  AND  blockerId = ?");
            statement.setString(1, String.valueOf(idBlocked));
            statement.setString(2, String.valueOf(idBlocker));
            statement.executeUpdate();
            return "success";
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return "fail";
    }


    public static String setAvatar(String ImageString, String userName) throws SQLException {
        String ImageString1 = ImageString;
        byte[] ImageByte = Base64.getDecoder().decode(ImageString1);
        Blob ImageBlob = new SerialBlob(ImageByte);
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://root:123456789@localhost:3306/twitter");
            PreparedStatement statement = connection.prepareStatement("update user set AVATAR = ? where USER_ID = ?");
            statement.setBlob(1, ImageBlob);
            statement.setString(2, userName);
            statement.executeUpdate();
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }

    public static String setHeader(String ImageString, String userName) throws SQLException {
        String ImageString1 = ImageString;
        byte[] ImageByte = Base64.getDecoder().decode(ImageString1);
        Blob ImageBlob = new SerialBlob(ImageByte);
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://root:123456789@localhost:3306/twitter");
            PreparedStatement statement = connection.prepareStatement("update user set Heder = ? where USER_ID = ?");
            statement.setBlob(1, ImageBlob);
            statement.setString(2, userName);
            statement.executeUpdate();
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }

    }

    public static User getUser(String userName) {
        Connection connection = null;
        User user = new User();
        try {
            connection = DriverManager.getConnection("jdbc:mysql://root:123456789@localhost:3306/twitter");
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM user");
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                if (userName.equals(result.getString(14))) {
                    Blob avatarBlob = result.getBlob("AVATAR");
                    byte[] avatarByte = avatarBlob.getBytes(1, (int) avatarBlob.length());
                    String avatarAsString = Base64.getEncoder().encodeToString(avatarByte);
                    Blob headerBlob = result.getBlob("HEDER");
                    byte[] headerByte = headerBlob.getBytes(1, (int) headerBlob.length());
                    String headerAsString = Base64.getEncoder().encodeToString(headerByte);
                    user.setAvatar(avatarAsString);
                    user.setBio(result.getString(4));
                    user.setUserName(result.getString(14));
                    user.setFirstName(result.getString(5));
                    user.setLastName(result.getString(6));
                    user.setHeader(headerAsString);
                    user.setSignUpDate(String.valueOf(result.getDate(10)));
                    user.setFollowers(result.getInt(16));
                    user.setFollowings(result.getInt(17));
                    user.setLocation(result.getString(12));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public static ArrayList<Tweet> TimeLine(String userName) throws SQLException {
        ArrayList<Tweet> selectedTwitter = new ArrayList<>();
        Connection connection = DriverManager.getConnection("jdbc:mysql://root:123456789@localhost:3306/twitter");
        ;
        PreparedStatement statement;
        statement = connection.prepareStatement("SELECT * FROM tweet");
        ResultSet result;
        result = statement.executeQuery();
        while (result.next()) {
            if (result.getInt(2) >= 10) {
                PreparedStatement statement1 = connection.prepareStatement("SELECT * FROM Favstar");
                ResultSet result1 = statement1.executeQuery();
                int chek = 1;
                while (result1.next()) {
                    if (result1.getInt(1) == result.getInt(7)) {
                        chek = 0;
                    }
                }
                if (chek == 1) {
                    PreparedStatement statement2 = connection.prepareStatement("INSERT into Favstar ( tweetId ) values (?) ");
                    statement2.setInt(1, result.getInt(7));
                    statement2.executeUpdate();
                }
            }
        }
        int idUser = search(userName);
        try {
            connection = DriverManager.getConnection("jdbc:mysql://root:123456789@localhost:3306/twitter");
            statement = connection.prepareStatement("SELECT * FROM follow where follower = ?");
            statement.setInt(1, idUser);
            result = statement.executeQuery();
            while (result.next()) {
                PreparedStatement statement1 = connection.prepareStatement("SELECT * FROM tweet where idUser = ?");
                statement1.setInt(1, result.getInt(1));
                ResultSet result1 = statement1.executeQuery();
                if (result1.next()) {
                    Blob avatarBlob = result1.getBlob("AVATAR");
                    byte[] avatarByte = avatarBlob.getBytes(1, (int) avatarBlob.length());
                    String avatarAsString = Base64.getEncoder().encodeToString(avatarByte);

                    Tweet tweet = new Tweet(result1.getString(1), result1.getString(9), result1.getString(5), avatarAsString, result1.getInt(2), result1.getInt(3), result1.getInt(4), result1.getInt(7));
                    ;
                    selectedTwitter.add(tweet);
                }
            }
            statement = connection.prepareStatement("SELECT * FROM favstar");
            result = statement.executeQuery();
            while (result.next()) {
                PreparedStatement statement2 = connection.prepareStatement("SELECT * FROM block where blockerId = ?");
                statement2.setInt(1, idUser);
                ResultSet result1 = statement2.executeQuery();
                int chek = 1;
                while (result1.next()) {
                    if (result.getInt(2) == result1.getInt(1)) {
                        chek = 0;
                    }
                }
                if (chek == 1) {
                    PreparedStatement statement3 = connection.prepareStatement("SELECT * FROM tweet where tweetId = ?");
                    statement3.setInt(1, result.getInt(1));
                    ResultSet result2 = statement3.executeQuery();
                    if (result2.next()) {
                        Blob avatarBlob = result2.getBlob("AVATAR");
                        byte[] avatarByte = avatarBlob.getBytes(1, (int) avatarBlob.length());
                        String avatarAsString = Base64.getEncoder().encodeToString(avatarByte);
                        Tweet tweet = new Tweet(result2.getString(1), result2.getString(9), result2.getString(5), avatarAsString, result2.getInt(2), result2.getInt(3), result2.getInt(4), result2.getInt(7));
                        ;
                        selectedTwitter.add(tweet);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return selectedTwitter;
    }

    public static ArrayList<User> searchUser(String key) {

        ArrayList<User> users = new ArrayList<>();
        Connection connection = null;
        String HasghtaG = key;
        User user = new User();
        try {
            connection = DriverManager.getConnection("jdbc:mysql://root:123456789@localhost:3306/twitter");
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM user");
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                String H = result.getString(5);
                String H1 = result.getString(6);
                String H2 = result.getString(14);
                if (H.equals(HasghtaG) || H1.equals(HasghtaG) || H2.equals(HasghtaG)) {
                    Blob avatarBlob = result.getBlob("AVATAR");
                    byte[] avatarByte = avatarBlob.getBytes(1, (int) avatarBlob.length());
                    String avatarAsString = Base64.getEncoder().encodeToString(avatarByte);
                    Blob headerBlob = result.getBlob("HEDER");
                    byte[] headerByte = headerBlob.getBytes(1, (int) headerBlob.length());
                    String headerAsString = Base64.getEncoder().encodeToString(headerByte);
                    user.setAvatar(avatarAsString);
                    user.setBio(result.getString(4));
                    user.setUserName(result.getString(14));
                    user.setFirstName(result.getString(5));
                    user.setLastName(result.getString(6));
                    user.setHeader(headerAsString);
                    user.setSignUpDate(String.valueOf(result.getDate(10)));
                    user.setFollowers(result.getInt(16));
                    user.setFollowings(result.getInt(17));
                    user.setLocation(result.getString(12));
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;

    }

    public static ArrayList<Tweet> searchHashtag(String key) {
        ArrayList<Tweet> tweets = new ArrayList<>();

        Connection connection = null;
        String HasghtaG = key;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://root:123456789@localhost:3306/twitter");
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM tweet");
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                String H = result.getString(1);
                if (H.contains(HasghtaG)) {
                    Blob avatarBlob = result.getBlob("AVATAR");
                    byte[] avatarByte = avatarBlob.getBytes(1, (int) avatarBlob.length());
                    String avatarAsString = Base64.getEncoder().encodeToString(avatarByte);
                    Tweet tweet = new Tweet(result.getString(1), result.getString(9), result.getString(5), avatarAsString, result.getInt(2), result.getInt(3), result.getInt(4), result.getInt(7));
                    tweets.add(tweet);

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tweets;


    }

    public static String changeTweetAvatar(String ImageString, String userName) throws SQLException {
        String ImageString1 = ImageString;
        byte[] ImageByte = Base64.getDecoder().decode(ImageString1);
        Blob ImageBlob = new SerialBlob(ImageByte);
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://root:123456789@localhost:3306/twitter");
            PreparedStatement statement = connection.prepareStatement("update tweet set AVATAR = ? where UserName = ?");
            statement.setBlob(1, ImageBlob);
            statement.setString(2, userName);
            statement.executeUpdate();
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }

    }

    public static String changeCommentAvatar(String ImageString, String userName) throws SQLException {
        String ImageString1 = ImageString;
        byte[] ImageByte = Base64.getDecoder().decode(ImageString1);
        Blob ImageBlob = new SerialBlob(ImageByte);
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://root:123456789@localhost:3306/twitter");
            PreparedStatement statement = connection.prepareStatement("update comment set prfile = ? where UserName = ?");
            statement.setBlob(1, ImageBlob);
            statement.setString(2, userName);
            statement.executeUpdate();
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }

    }

    public static ArrayList<Reply> commentView(int tweetId) {
        ArrayList<Reply> selectedComment = new ArrayList<>();
        Connection connection = null;
        //int idUser = 10 ;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://root:123456789@localhost:3306/twitter");
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM comment where idTweet = ?");
            statement.setInt(1, tweetId);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                Blob avatarBlob = result.getBlob("prfile");
                byte[] avatarByte = avatarBlob.getBytes(1, (int) avatarBlob.length());
                String avatarAsString = Base64.getEncoder().encodeToString(avatarByte);
                Reply reply = new Reply(result.getString(1), result.getString(6), result.getString(4), avatarAsString);
                selectedComment.add(reply);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return selectedComment;
    }

    public static Tweet getTweet(int tweetId) {
        Connection connection = null;
        Tweet tweet = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://root:123456789@localhost:3306/twitter");
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM tweet where tweetId = ?");
            statement.setInt(1, tweetId);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                Blob avatarBlob = result.getBlob("AVATAR");
                byte[] avatarByte = avatarBlob.getBytes(1, (int) avatarBlob.length());
                String avatarAsString = Base64.getEncoder().encodeToString(avatarByte);
                tweet = new Tweet(result.getString(1), result.getString(9), result.getString(5), avatarAsString, result.getInt(2), result.getInt(3), result.getInt(4), result.getInt(7));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tweet;

    }

    public static List<User> followerView(String userName) {
        ArrayList<Integer> selectedUseres = new ArrayList<>();
        ArrayList<User> selectedUseres1 = new ArrayList<>();
        Connection connection = null;
        int userId = search(userName);
        try {
            connection = DriverManager.getConnection("jdbc:mysql://root:123456789@localhost:3306/twitter");
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM follow where followed = ?");
            statement.setInt(1, userId);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                selectedUseres.add(result.getInt(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection("jdbc:mysql://root:123456789@localhost:3306/twitter");
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM user");
            ResultSet result = statement.executeQuery();
            for (int x : selectedUseres) {
                statement = connection.prepareStatement("SELECT * FROM user where ID = ?");
                statement.setInt(1, x);
                result = statement.executeQuery();
                if (result.next()) {
                    User user = new User();
                    Blob avatarBlob = result.getBlob("AVATAR");
                    byte[] avatarByte = avatarBlob.getBytes(1, (int) avatarBlob.length());
                    String avatarAsString = Base64.getEncoder().encodeToString(avatarByte);
                    Blob headerBlob = result.getBlob("HEDER");
                    byte[] headerByte = headerBlob.getBytes(1, (int) headerBlob.length());
                    String headerAsString = Base64.getEncoder().encodeToString(headerByte);
                    user.setAvatar(avatarAsString);
                    user.setBio(result.getString(4));
                    user.setUserName(result.getString(14));
                    user.setFirstName(result.getString(5));
                    user.setLastName(result.getString(6));
                    user.setHeader(headerAsString);
                    user.setSignUpDate(String.valueOf(result.getDate(10)));
                    user.setFollowers(result.getInt(16));
                    user.setFollowings(result.getInt(17));
                    user.setLocation(result.getString(12));
                    selectedUseres1.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return selectedUseres1;
    }


    public static List<User> followingView(String userName) {
        ArrayList<Integer> selectedUseres = new ArrayList<>();
        ArrayList<User> selectedUseres1 = new ArrayList<>();
        Connection connection = null;
        int userId = search(userName);
        try {
            connection = DriverManager.getConnection("jdbc:mysql://root:123456789@localhost:3306/twitter");
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM follow where follower = ?");
            statement.setInt(1, userId);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                selectedUseres.add(result.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection("jdbc:mysql://root:123456789@localhost:3306/twitter");
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM user");
            ResultSet result = statement.executeQuery();
            for (int x : selectedUseres) {
                statement = connection.prepareStatement("SELECT * FROM user where ID = ?");
                statement.setInt(1, x);
                result = statement.executeQuery();
                if (result.next()) {
                    User user = new User();
                    Blob avatarBlob = result.getBlob("AVATAR");
                    byte[] avatarByte = avatarBlob.getBytes(1, (int) avatarBlob.length());
                    String avatarAsString = Base64.getEncoder().encodeToString(avatarByte);
                    Blob headerBlob = result.getBlob("HEDER");
                    byte[] headerByte = headerBlob.getBytes(1, (int) headerBlob.length());
                    String headerAsString = Base64.getEncoder().encodeToString(headerByte);
                    user.setAvatar(avatarAsString);
                    user.setBio(result.getString(4));
                    user.setUserName(result.getString(14));
                    user.setFirstName(result.getString(5));
                    user.setLastName(result.getString(6));
                    user.setHeader(headerAsString);
                    user.setSignUpDate(String.valueOf(result.getDate(10)));
                    user.setFollowers(result.getInt(16));
                    user.setFollowings(result.getInt(17));
                    user.setLocation(result.getString(12));
                    selectedUseres1.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return selectedUseres1;
    }

    public static Boolean checkBlock(String blockerUser, String blockedUser) {
        Connection connection = null;
        int blocker = search(blockerUser);
        int blocked = search(blockedUser);
        try {
            connection = DriverManager.getConnection("jdbc:mysql://root:123456789@localhost:3306/twitter");
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM block ");
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                if (blocker == result.getInt(2) && blocked == result.getInt(1)) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
