package com.Regex;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test1 {
    public static void main(String[] args) {
        // ����Ŀ��Ŀ¼
        System.out.println("��������Ŀ�ļ�Ŀ¼��");
        Scanner sc = new Scanner(System.in);
        String FilePath = sc.nextLine();
        sc.close();

        try (BufferedReader br = new BufferedReader(
                new FileReader(FilePath)); // ������
        ) {
            char[] buffer = new char[1024];
            int len;
            StringBuilder strBuilder = new StringBuilder(); // ���ڴ洢ȫ����ȡ�ı����ַ���
            while ((len = br.read(buffer)) != -1) {
                String str = new String(buffer, 0, len);
                strBuilder.append(str);
            }

            // ����HTML�ļ�ͷ
            try (BufferedWriter bw = new BufferedWriter(
                    new FileWriter("D:\\Coding\\Java practise\\Homework2_Regex\\src\\com\\Tests\\taeri.html"))) {
                bw.write("<html><body>\n");
            }

            // ����������ʽ
            String phoneRegex = "\\b([0]\\d{2,3}[- ]?\\d{7,8})|([1][3-9]\\d{9})\\b";
            String URLRegex = "(\\b(https?|ftp)://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?\\b)|(\\b([^@\\s]+\\.)+[^@\\s]+(/[\\w-./?%&=]*)?\\b)";
            String IPV4Regex = "\\b(?:\\d{1,3}\\.){3}\\d{1,3}\\b";
            String EmailRegex = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b";

            // 1. �ȴ������䲢����HTML��ͬʱ��������ı����Ƴ�
            strBuilder = processAndRemoveMatches(strBuilder, EmailRegex, "mailto:");

            // 2. ����URL�����⽫��������ʶ��ΪURL
            strBuilder = processAndRemoveMatches(strBuilder, URLRegex, "");

            // 3. �����������绰��IP
            processAndRemoveMatches(strBuilder, phoneRegex, "tel:");
            processAndRemoveMatches(strBuilder, IPV4Regex, "http://");

            // ����HTML�ļ�β
            try (BufferedWriter bw = new BufferedWriter(
                    new FileWriter("D:\\Coding\\Java practise\\Homework2_Regex\\src\\com\\Tests\\taeri.html",
                            true))) {
                bw.write("</body></html>\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ƥ��������ʽ���������滻ΪHTML���ӣ�ͬʱ���ı����Ƴ�ƥ����
     * @param str �����ı�
     * @param regex ������ʽ
     * @param protocol URL������ǰ׺����mailto:��tel:��
     * @return �Ƴ�ƥ�����ݺ���ı�
     */
    public static StringBuilder processAndRemoveMatches(StringBuilder str, String regex, String protocol) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        StringBuilder updatedStr = new StringBuilder(str); // �����ı��������Ƴ���ƥ��Ĳ���

        // �洢�������ƫ��������Ϊ�Ƴ�������Ӱ��������
        int offset = 0;

        while (matcher.find()) {
            String match = matcher.group();
            String link;

            // �����Ƿ���ҪЭ����������
            if (protocol.isEmpty()) {
                link = "<a href=\"" + match + "\">" + match + "</a>";
            } else {
                link = "<a href=\"" + protocol + match + "\">" + match + "</a>";
            }

            // ������д��HTML�ļ�
            try (BufferedWriter bw = new BufferedWriter(
                    new FileWriter("D:\\Coding\\Java practise\\Homework2_Regex\\src\\com\\Tests\\taeri.html",
                            true))) {
                bw.write(link);
                bw.write("<br/>\n"); // ÿ��ƥ��������
            } catch (Exception e) {
                e.printStackTrace();
            }

            // ���ı����Ƴ���ƥ��Ĳ���
            int start = matcher.start() - offset; // ƫ�Ƶ�����Ŀ�ʼ����
            int end = matcher.end() - offset;     // ƫ�Ƶ�����Ľ�������
            updatedStr.replace(start, end, "");   // �滻Ϊ���ա�����ɾ��ƥ��Ĳ���

            // ��¼ƫ����
            offset += (end - start);
        }

        return updatedStr;
    }
}
