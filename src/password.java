import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class password {
    private static final SecureRandom RAND = new SecureRandom();
    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 256;

    public static Optional<String> genSalt (int length)
    {
        byte[] salt = new byte[length];
        RAND.nextBytes(salt);

        return Optional.of(Base64.getEncoder().encodeToString(salt));
    }

    public static Optional<String> hashPassword (char[] passwordIn, String salt)
    {
        byte[] bytes = salt.getBytes();

        PBEKeySpec spec = new PBEKeySpec(passwordIn, bytes, ITERATIONS, KEY_LENGTH);
        Arrays.fill(passwordIn, Character.MIN_VALUE);
        try {
            SecretKeyFactory factoryKey = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] securePassword = factoryKey.generateSecret(spec).getEncoded();
            return Optional.of(Base64.getEncoder().encodeToString(securePassword));
        }
        catch (NoSuchAlgorithmException | InvalidKeySpecException ex)
        {
            return Optional.empty();
        } finally {
            spec.clearPassword();
        }
    }

    public static  boolean verifyPassword ( char[] password, String key, String salt)
    {
        Optional<String> optEncrypted = hashPassword(password, salt);
        if (!optEncrypted.isPresent()){return false;}

        return optEncrypted.get().equals(key);
    }
}
