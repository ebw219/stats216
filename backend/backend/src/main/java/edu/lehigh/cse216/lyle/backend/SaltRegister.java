package edu.lehigh.cse216.lyle.backend;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class SaltRegister {

        public static byte[] saltReg(byte[] pass) throws NoSuchAlgorithmException, InvalidKeySpecException
        {
            String  originalPassword = "password"; //get password that they suggest
            String generatedSecuredPasswordHash = generateStorngPasswordHash(originalPassword);
            return generatedSecuredPasswordHash.getBytes();
        }
        public static String generateStorngPasswordHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException
        {
            int iterations = 1000;
            char[] chars = password.toCharArray();
            byte[] salt = getSalt();
            
            PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = skf.generateSecret(spec).getEncoded();
            return iterations + ":" + toHex(salt) + ":" + toHex(hash);
        }
        
        public static byte[] getSalt() throws NoSuchAlgorithmException
        {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            byte[] salt = new byte[16];
            sr.nextBytes(salt);
            System.out.println("salt: " + salt);
            return salt;
        }
        
        private static String toHex(byte[] array) throws NoSuchAlgorithmException
        {
            BigInteger bi = new BigInteger(1, array);
            String hex = bi.toString(16);
            int paddingLength = (array.length * 2) - hex.length();
            if(paddingLength > 0)
            {
                return String.format("%0"  +paddingLength + "d", 0) + hex;
            }else{
                return hex;
            }
        }
    }