package com.datamelt.utilities.datagenerator.utilities.transformation;

import com.datamelt.utilities.datagenerator.utilities.encrypt.EncryptionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.crypto.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class DataTransformer
{
    private static Logger logger = LoggerFactory.getLogger(DataTransformer.class);
    private static Cipher cipherEncrypt;

    static
    {
        try
        {
            cipherEncrypt = EncryptionHelper.getCypher();
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

    public static long negate(Long value)
    {
        return -value;
    }

    public static double negate(Double value)
    {
        return -value;
    }

    public static double round(Double value, Long decimalPlaces)
    {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(decimalPlaces.intValue(), RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static String encrypt(String value) throws BadPaddingException, IllegalBlockSizeException
    {
        byte[] cipherText = cipherEncrypt.doFinal(value.getBytes());
        return Base64.getEncoder().encodeToString(cipherText);
    }

    public static String decrypt(String value) throws BadPaddingException, IllegalBlockSizeException
    {
        //byte[] plainText = cipherDecrypt.doFinal(Base64.getDecoder().decode(value));
        //return new String(plainText);
        // TODO: implement later
        return null;
    }
}
