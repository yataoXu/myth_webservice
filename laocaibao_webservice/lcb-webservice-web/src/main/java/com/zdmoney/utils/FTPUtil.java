package com.zdmoney.utils;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.*;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class FTPUtil {    
    private FTPClient ftpClient;    
    public static final int BINARY_FILE_TYPE = FTP.BINARY_FILE_TYPE;    
    public static final int ASCII_FILE_TYPE = FTP.ASCII_FILE_TYPE;    
          
    public void connectServer(String host,String port,String user,String password,String remotePath) throws SocketException,    
            IOException {       
        connectServer(host, Integer.parseInt(port), user, password, remotePath);    
    }    
        
        
    public void connectServer(String server, int port, String user,    
            String password, String path) throws SocketException, IOException {    
        ftpClient = new FTPClient();    
        ftpClient.connect(server, port);    
        System.out.println("Connected to " + server + ".");    
        System.out.println(ftpClient.getReplyCode());    
        ftpClient.login(user, password);    
        // Path is the sub-path of the FTP path    
        if (path.length() != 0) {    
            ftpClient.changeWorkingDirectory(path);    
        }    
    }    
 
    public void setFileType(int fileType) throws IOException {    
        ftpClient.setFileType(fileType);    
    }    
    
    public void closeServer() throws IOException {    
        if (ftpClient != null && ftpClient.isConnected()) {
            ftpClient.disconnect();    
        }    
    }    
        
    public boolean changeDirectory(String path) throws IOException {    
        return ftpClient.changeWorkingDirectory(path);    
    }    
    public boolean createDirectory(String pathName) throws IOException {    
        return ftpClient.makeDirectory(pathName);    
    }    
    public boolean removeDirectory(String path) throws IOException {    
        return ftpClient.removeDirectory(path);    
    }    
          
    public boolean removeDirectory(String path, boolean isAll)    
            throws IOException {    
            
        if (!isAll) {    
            return removeDirectory(path);    
        }    
    
        FTPFile[] ftpFileArr = ftpClient.listFiles(path);    
        if (ftpFileArr == null || ftpFileArr.length == 0) {    
            return removeDirectory(path);    
        }    
        //     
        for (FTPFile ftpFile : ftpFileArr) {    
            String name = ftpFile.getName();    
            if (ftpFile.isDirectory()) {    
            	System.out.println("* [sD]Delete subPath ["+path + "/" + name+"]");                 
                removeDirectory(path + "/" + name, true);    
            } else if (ftpFile.isFile()) {    
            	System.out.println("* [sF]Delete file ["+path + "/" + name+"]");                            
                deleteFile(path + "/" + name);    
            } else if (ftpFile.isSymbolicLink()) {    
    
            } else if (ftpFile.isUnknown()) {    
    
            }    
        }    
        return ftpClient.removeDirectory(path);    
    }    
        
    // Check the path is exist; exist return true, else false.    
    public boolean existDirectory(String path) throws IOException {    
        boolean flag = false;    
        FTPFile[] ftpFileArr = ftpClient.listFiles(path);    
        for (FTPFile ftpFile : ftpFileArr) {    
            if (ftpFile.isDirectory()    
                    && ftpFile.getName().equalsIgnoreCase(path)) {    
                flag = true;    
                break;    
            }    
        }    
        return flag;    
    }    
    
    public List<String> getFileList(String path) throws IOException {      
        FTPFile[] ftpFiles= ftpClient.listFiles(path);    
            
        List<String> retList = new ArrayList<String>();    
        if (ftpFiles == null || ftpFiles.length == 0) {    
            return retList;    
        }    
        for (FTPFile ftpFile : ftpFiles) {    
            if (ftpFile.isFile()) {    
                retList.add(ftpFile.getName());    
            }    
        }    
        return retList;    
    }    
    
    public boolean deleteFile(String pathName) throws IOException {    
        return ftpClient.deleteFile(pathName);    
    }    
    
    
    public boolean uploadFile(String fileName, String newName)    
            throws IOException {    
        boolean flag = false;    
        InputStream iStream = null;    
        try {    
            iStream = new FileInputStream(fileName);    
            flag = ftpClient.storeFile(newName, iStream);    
        } catch (IOException e) {    
            flag = false;    
            return flag;    
        } finally {    
            if (iStream != null) {    
                iStream.close();    
            }    
        }    
        return flag;    
    }    
    
    public boolean uploadFile(String fileName) throws IOException {    
        return uploadFile(fileName, fileName);    
    }    
    
    public boolean uploadFile(InputStream iStream, String newName)    
            throws IOException {    
        boolean flag = false;    
        try {    
            flag = ftpClient.storeFile(newName, iStream);    
        } catch (IOException e) {    
            flag = false;    
            return flag;    
        } finally {    
            if (iStream != null) {    
                iStream.close();    
            }    
        }    
        return flag;    
    }    
    
    public boolean uploadImgFile(InputStream iStream, String newName)    
            throws IOException {    
        boolean flag = false;    
        try {    
        	ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE); 
            flag = ftpClient.storeFile(newName, iStream);    
        } catch (IOException e) {    
            flag = false;    
            return flag;    
        } finally {    
            if (iStream != null) {    
                iStream.close();    
            }    
        }    
        return flag;    
    }    
    
    public boolean download(String remoteFileName, String localFileName)    
            throws IOException {    
        boolean flag = false;    
        File outfile = new File(localFileName);    
        OutputStream oStream = null;    
        try {    
            oStream = new FileOutputStream(outfile);    
            flag = ftpClient.retrieveFile(remoteFileName, oStream);    
        } catch (IOException e) {    
            flag = false;    
            return flag;    
        } finally {    
            oStream.close();    
        }    
        return flag;    
    }    
        
    public InputStream downFile(String sourceFileName) throws IOException {    
        return ftpClient.retrieveFileStream(sourceFileName);    
    }    
    
    public InputStream downImgFile(String sourceFileName) throws IOException {  
    	ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE); 
        return ftpClient.retrieveFileStream(sourceFileName);    
    }    
}   

