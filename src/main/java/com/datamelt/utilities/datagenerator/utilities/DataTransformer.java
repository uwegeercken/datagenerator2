package com.datamelt.utilities.datagenerator.utilities;

import com.datamelt.utilities.datagenerator.config.model.options.RandomStringOptions;
import com.datamelt.utilities.datagenerator.generate.RandomStringGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Random;

public class DataTransformer
{
    private static Logger logger = LoggerFactory.getLogger(DataTransformer.class);
    private static final String ENCRYPTION_ALGORITHM = "AES/CBC/PKCS5Padding";

    private static Cipher cipherEncrypt;
    private static Cipher cipherDecrypt;

    static
    {
        logger.info("preparing encryption...");
        try
        {
            SecretKey key = getKeyFromPassword("blabla", generateRandomSalt());
            IvParameterSpec iv = generateIv();
            cipherEncrypt = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            cipherEncrypt.init(Cipher.ENCRYPT_MODE, key, iv);
            cipherDecrypt = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            cipherDecrypt.init(Cipher.DECRYPT_MODE, key, iv);
        }
        catch(Exception ex )
        {
            logger.error("error initializing encryption: ",ex.getMessage());
            throw new RuntimeException("error initializing encryption - aborting");
        }
    }

    public static String lowercase(String value)
    {
        return value.toLowerCase();
    }

    public static String uppercase(String value)
    {
        return value.toUpperCase();
    }

    public static String base64encode(String value)
    {
        return Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    public static String reverse(String value)
    {
        StringBuffer buffer = new StringBuffer();
        for(int i=value.length()-1;i>=0;i--)
        {
            buffer.append(value.substring(i, i+1));
        }
        return buffer.toString();
    }

    public static String prepend(String value, String prefix)
    {
        return prefix + value;
    }

    public static String append(String value, String suffix)
    {
        return value + suffix;
    }

    public static long negate(long value)
    {
        return -value;
    }

    public static String encrypt(String value) throws BadPaddingException, IllegalBlockSizeException
    {
        byte[] cipherText = cipherEncrypt.doFinal(value.getBytes());
        return Base64.getEncoder().encodeToString(cipherText);
    }

    public static String decrypt(String value) throws BadPaddingException, IllegalBlockSizeException
    {
        byte[] plainText = cipherDecrypt.doFinal(Base64.getDecoder().decode(value));
        return new String(plainText);
    }

    private static SecretKey generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256);
        SecretKey key = keyGenerator.generateKey();
        return key;
    }

    private static SecretKey getKeyFromPassword(String password, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
        SecretKey secret = new SecretKeySpec(factory.generateSecret(spec)
                .getEncoded(), "AES");
        return secret;
    }

    private static IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    private static String generateRandomSalt()
    {
        Random random = new Random();
        long randomLength = random.nextLong(0,40);
        StringBuffer randomString = new StringBuffer();
        String randomCharacters = (String) RandomStringOptions.RANDOM_CHARACTERS.getDefaultValue();
        for(long i=0;i<randomLength;i++)
        {
            int position = random.nextInt(randomCharacters.length());
            randomString.append(randomCharacters.substring(position, position+1));
        }
        return randomString.toString();
    }
}
