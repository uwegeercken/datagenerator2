package com.datamelt.utilities.datagenerator.utilities.encrypt;

import com.datamelt.utilities.datagenerator.error.Failure;
import com.datamelt.utilities.datagenerator.error.Success;
import com.datamelt.utilities.datagenerator.error.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Random;

public class EncryptionHelper
{
    private static final Logger logger = LoggerFactory.getLogger(EncryptionHelper.class);
    public static final String ENCRYPTION_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String AVAILABLE_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPRSTUVWXYZ0123456789#+-!";

    private static Try<Cipher> cipher;

    private static void setupCipher()
    {
        logger.info("preparing encryption using " + EncryptionHelper.ENCRYPTION_ALGORITHM + " ...");
        try
        {
            SecretKey key = getKeyFromPassword(generateRandomPassword());
            IvParameterSpec iv = generateIv();
            Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            EncryptionHelper.cipher = new Success<>(cipher);
        }
        catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | InvalidAlgorithmParameterException | InvalidKeyException ex)
        {
            logger.error("error initializing encryption: [{}]", ex.getMessage());
            cipher = new Failure<>(ex);
        }
    }

    private static SecretKey getKeyFromPassword(String password) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), "7684".getBytes(), 65536, 256);
        return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
    }

    private static IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    private static String generateRandomPassword()
    {
        Random random = new Random();
        long randomLength = random.nextLong(0,40);
        StringBuilder randomString = new StringBuilder();
        for(long i=0;i<randomLength;i++)
        {
            int position = random.nextInt(AVAILABLE_CHARACTERS.length());
            randomString.append(AVAILABLE_CHARACTERS.substring(position, position+1));
        }
        return randomString.toString();
    }

    public static String encrypt(String value)
    {
        if(cipher == null)
        {
            setupCipher();
        }
        if(cipher instanceof Success<?>)
        {
            try
            {
                byte[] cipherText = cipher.getResult().doFinal(value.getBytes());
                return Base64.getEncoder().encodeToString(cipherText);
            }
            catch (BadPaddingException | IllegalBlockSizeException ex)
            {
                logger.error("error encrypting value: [{}]", ex.getMessage());
                return value;
            }
        }
        else
        {
            return value;
        }
    }
}
