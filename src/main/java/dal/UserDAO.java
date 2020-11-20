package dal;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    private final SQLDatabaseIO db = new SQLDatabaseIO("kamel", "dreng", "runerne.dk", 8003);

    public UserDAO() {
    }

    public String authenticateUser(String email, String password) throws SQLException, Exception {
        ResultSet rs = db.query("SELECT passHash, salt FROM user WHERE email = ?", new String[] { email });
        rs.next();

        String hash = rs.getString("passHash");
        String salt = rs.getString("salt");

        rs.close();

        if (hash.equals(generatePassHash(password, convertSaltToByteArray(salt)))) {
            return "New generated session id that's added to database";
        } else {
            throw new Exception("Unable to authenticate");
        }
    }

    private byte[] convertSaltToByteArray(String saltStr) {
        byte[] salt = new byte[16]; //converting salt to byte arr
        for (int i = 0; i < 16; i++){
            int first = 2 * i;
            int second = first + 1;
            String hex = saltStr.charAt(first)  + "" + saltStr.charAt(second); //getting the next 2 hexadecimals to convert to byte
            long x = Long.parseLong(hex, 16) - 128; //Converting back to having numbers between -128 - 127
            salt[i] = (byte) x;
        }

        return salt;
    }

    private String generatePassHash(String pass, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeySpec spec = new PBEKeySpec(pass.toCharArray(), salt, 65536, 128);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] passHash = factory.generateSecret(spec).getEncoded();
        String genPass = "";
        for (int i = 0; i < 16; i++) {
            String gen = Integer.toHexString(passHash[i] + 128); //adding 128 to have numbers between 0 - 255
            if (gen.length() < 2) {
                gen = "0" + gen;
            }
            genPass += gen;
        }
        return genPass;
    }
}
