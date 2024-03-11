package com.datamelt.utilities.datagenerator.utilities.encrypt;

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
import java.util.Random;

public class EncryptionHelper
{
    public static final String ENCRYPTION_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String AVAILABLE_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPRSTUVWXYZ0123456789#+-!";
    public static Cipher getCypher() throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException
    {
        SecretKey key = getKeyFromPassword(generateRandomPassword());
        IvParameterSpec iv = generateIv();
        Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        return cipher;
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
}
