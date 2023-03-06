package io.github.fairyspace.qywechat.utils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.*;


public class FileUtil {
    public final static Map<String, String> FILE_TYPE_MAP = new HashMap<String, String>();

    static {
        FILE_TYPE_MAP.put("FFD8FFE0", "jpg"); // JPEG (jpg)
        FILE_TYPE_MAP.put("31313131", "txt"); // JPEG (jpg)
        FILE_TYPE_MAP.put("52494646", "webp"); // JPEG (jpg)
        FILE_TYPE_MAP.put("FFD8FFE1", "jpeg"); // JPEG (jpg)
        FILE_TYPE_MAP.put("89504E47", "png"); // PNG (png)
        FILE_TYPE_MAP.put("47494638", "gif"); // GIF (gif)
        FILE_TYPE_MAP.put("49492a00227105008037", "tif"); // TIFF (tif)
        FILE_TYPE_MAP.put("424d228c010000000000", "bmp"); // 16色位图(bmp)
        FILE_TYPE_MAP.put("424d8240090000000000", "bmp"); // 24位位图(bmp)
        FILE_TYPE_MAP.put("424d8e1b030000000000", "bmp"); // 256色位图(bmp)
        FILE_TYPE_MAP.put("41433130313500000000", "dwg"); // CAD (dwg)
        FILE_TYPE_MAP.put("68746D6C3E", "html"); // HTML (html)
        FILE_TYPE_MAP.put("48544d4c207b0d0a0942", "css"); // css
        FILE_TYPE_MAP.put("696b2e71623d696b2e71", "js"); // js
        FILE_TYPE_MAP.put("7b5c727466315c616e73", "rtf"); // Rich Text Format
        // (rtf)
        FILE_TYPE_MAP.put("38425053000100000000", "psd"); // Photoshop (psd)
        FILE_TYPE_MAP.put("44656C69766572792D646174653A", "eml"); // Email
        FILE_TYPE_MAP.put("d0cf11e0a1b11ae10000", "doc"); // MS Excel
        // 注意：word、msi 和
        // excel的文件头一样
        FILE_TYPE_MAP.put("d0cf11e0a1b11ae10000", "vsd"); // Visio 绘图
        FILE_TYPE_MAP.put("5374616E64617264204A", "mdb"); // MS Access (mdb)
        FILE_TYPE_MAP.put("252150532D41646F6265", "ps");
        FILE_TYPE_MAP.put("255044462d312e", "pdf");
        FILE_TYPE_MAP.put("75736167", "txt");
        FILE_TYPE_MAP.put("2e524d46000000120001", "rmvb"); // rmvb/rm相同
        FILE_TYPE_MAP.put("464c5601050000000900", "flv"); // flv与f4v相同
        FILE_TYPE_MAP.put("00000020667479706d70", "mp4");
        FILE_TYPE_MAP.put("49443303000000002176", "mp3");
        FILE_TYPE_MAP.put("000001b", "mpg"); //MPEG (mpg)，文件头：000001BA MPEG (mpg)，文件头：000001B3
        FILE_TYPE_MAP.put("3026b2758e66cf11a6d9", "wmv"); // wmv与asf相同
        FILE_TYPE_MAP.put("57415645", "wav"); // Wave (wav)
        FILE_TYPE_MAP.put("41564920", "avi");
        FILE_TYPE_MAP.put("4d546864", "mid"); // MIDI (mid)
        FILE_TYPE_MAP.put("504b0304", "zip");
        FILE_TYPE_MAP.put("52617221", "rar");
        FILE_TYPE_MAP.put("235468697320636f6e66", "ini");
        FILE_TYPE_MAP.put("504b03040a0000000000", "jar");
        FILE_TYPE_MAP.put("4d5a9000030000000400", "exe");// 可执行文件
        FILE_TYPE_MAP.put("3c25402070616765206c", "jsp");// jsp文件
        FILE_TYPE_MAP.put("4d616e69666573742d56", "mf");// MF文件
        FILE_TYPE_MAP.put("3C3F786D6C", "xml");// xml文件
        FILE_TYPE_MAP.put("494e5345525420494e54", "sql");// xml文件
        FILE_TYPE_MAP.put("7061636b616765207765", "java");// java文件
        FILE_TYPE_MAP.put("406563686f206f66660d", "bat");// bat文件
        FILE_TYPE_MAP.put("1f8b0800000000000000", "gz");// gz文件
        FILE_TYPE_MAP.put("6c6f67346a2e726f6f74", "properties");// bat文件
        FILE_TYPE_MAP.put("cafebabe0000002e0041", "class");// bat文件
        FILE_TYPE_MAP.put("49545346030000006000", "chm");// bat文件
        FILE_TYPE_MAP.put("04000000010000001300", "mxp");// bat文件
        FILE_TYPE_MAP.put("504b0304140006000800", "docx");// docx文件
        FILE_TYPE_MAP.put("d0cf11e0a1b11ae10000", "wps");// WPS文字wps、表格et、演示dps都是一样的
        FILE_TYPE_MAP.put("6431303a637265617465", "torrent");
        FILE_TYPE_MAP.put("6D6F6F76", "mov"); // Quicktime (mov)
        FILE_TYPE_MAP.put("FF575043", "wpd"); // WordPerfect (wpd)
        FILE_TYPE_MAP.put("CFAD12FEC5FD746F", "dbx"); // Outlook Express (dbx)
        FILE_TYPE_MAP.put("2142444E", "pst"); // Outlook (pst)
        FILE_TYPE_MAP.put("AC9EBD8F", "qdf"); // Quicken (qdf)
        FILE_TYPE_MAP.put("E3828596", "pwl"); // Windows Password (pwl)
        FILE_TYPE_MAP.put("2E7261FD", "ram"); // Real Audio (ram)
        FILE_TYPE_MAP.put("2E524D46", "rm");
    }

    public static String getFileType(File file) {
        if (!file.exists() || !file.isFile()) {
            throw new RuntimeException("路径不存在或者该路径可能是目录");
        }
        String res = null;
        try (FileInputStream in = new FileInputStream(file)) {
            byte[] b = new byte[4];
            in.read(b, 0, b.length);
            String fileCode = bytesToHexString(b);
            // 这种方法在字典的头代码不够位数的时候可以用但是速度相对慢一点
            Iterator<String> keyIter = FILE_TYPE_MAP.keySet().iterator();
            while (keyIter.hasNext()) {
                String key = keyIter.next();
                if (key.toUpperCase().startsWith(fileCode)) {
                    res = FILE_TYPE_MAP.get(key);
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("文件找不到", e);
        } catch (IOException e) {
            throw new RuntimeException("文件读取异常", e);
        }
        return res;
    }

    public static String getMediaType(byte[] bytes) {
        String res = "jpg";
        byte[] dest = Arrays.copyOfRange(bytes, 0, 4);
        String fileCode = bytesToHexString(dest);
        // 这种方法在字典的头代码不够位数的时候可以用但是速度相对慢一点
        Iterator<String> keyIter = FILE_TYPE_MAP.keySet().iterator();
        while (keyIter.hasNext()) {
            String key = keyIter.next();
            if (key.toUpperCase().startsWith(fileCode)) {
                res = FILE_TYPE_MAP.get(key);
                break;
            }
        }
        return res;
    }


    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF; // 去除高位0， 03，80
            String hv = Integer.toHexString(v).toUpperCase(); // 以十六进制（基数 16）无符号整数形式返回一个整数参数的字符串表示形式，并转换为大写
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }


    public static File bytes2File(byte[] bytes, String filePath) {
        File tempFile = new File(filePath);//创建文件保存图片
        try {
            FileOutputStream outputStream = new FileOutputStream(tempFile);//创建输出流
            outputStream.write(bytes);//写入数据
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tempFile;
    }


    public static byte[] file2Bytes(File file) {
        byte[] buffer = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    public static byte[] inputStream2Bytes(InputStream inputStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[2048];//创建一个buffer字符串
        int len;
        //使用一个输入流从buffer里把数据读取出来
        while ((len = inputStream.read(buffer)) != -1) {
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        //把outStream里的数据写入内存
        return outStream.toByteArray();
    }


    public static String createFileName(String prefix, String suffix) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return (prefix == null ? "" : prefix) + simpleDateFormat.format(new Date()) + (int) (Math.random() * 900 + 100) + (suffix == null ? "" : suffix);
    }

    public static String createFileName() {
        return createFileName(null, null);
    }


    public static InputStream ur2InputStream(String urlStr) {
        try {
            URL url = new URL(urlStr);//定义一个url对象
            URLConnection urlConnection = url.openConnection();
            urlConnection.setRequestProperty("User-Agent", "Mozilla/4.76");
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);
            InputStream inputStream = urlConnection.getInputStream();
            return inputStream;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * @param urlStr
     * @param destinationDirectory 为空，则默认放在根目录
     * @param fileName             可以为空，则自动生成
     * @return File 文件
     */
    public static File url2File(String urlStr, String destinationDirectory, String fileName) {
        File tempFile = null;
        String filePath = "";
        InputStream inputStream = null;
        try {
            inputStream = ur2InputStream(urlStr);
            byte[] bytes = inputStream2Bytes(inputStream);
            if (destinationDirectory != null) {
                filePath = destinationDirectory + (destinationDirectory.endsWith(File.separator) ? "" : File.separator);
            }
            if (fileName == null || fileName.equals("")) {
                String mediaType = getMediaType(bytes);
                fileName = createFileName(null, "." + mediaType);
            }
            filePath += fileName;
            tempFile = bytes2File(bytes, filePath);

            System.out.println("file downloaded successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return tempFile;
    }
}
