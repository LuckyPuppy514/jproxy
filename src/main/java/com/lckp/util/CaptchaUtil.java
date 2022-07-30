/**
 * @Title: CaptchaUtil.java
 * @version V1.0
 */
package com.lckp.util;

import java.awt.Font;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pig4cloud.captcha.SpecCaptcha;
import com.pig4cloud.captcha.base.Captcha;

/**
 * @className: CaptchaUtil
 * @description: 图形验证码工具类
 * @date 2022年7月19日
 * @author LuckyPuppy514
 */
public class CaptchaUtil {
    private static final String SESSION_KEY = "captcha";
    private static final int DEFAULT_LEN = 4;
    private static final int DEFAULT_WIDTH = 130;
    private static final int DEFAULT_HEIGHT = 48;

    public CaptchaUtil() {
    }

    public static void out(HttpServletRequest request, HttpServletResponse response) throws IOException {
        out(DEFAULT_LEN, request, response);
    }

    public static void out(int len, HttpServletRequest request, HttpServletResponse response) throws IOException {
        out(DEFAULT_WIDTH, DEFAULT_HEIGHT, len, request, response);
    }

    public static void out(int width, int height, int len, HttpServletRequest request, HttpServletResponse response) throws IOException {
        out(width, height, len, (Font)null, request, response);
    }

    public static void out(Font font, HttpServletRequest request, HttpServletResponse response) throws IOException {
        out(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_LEN, font, request, response);
    }

    public static void out(int len, Font font, HttpServletRequest request, HttpServletResponse response) throws IOException {
        out(DEFAULT_WIDTH, DEFAULT_HEIGHT, len, font, request, response);
    }

    public static void out(int width, int height, int len, Font font, HttpServletRequest request, HttpServletResponse response) throws IOException {
        SpecCaptcha specCaptcha = new SpecCaptcha(width, height, len);
        specCaptcha.setCharType(Captcha.TYPE_ONLY_NUMBER);
        if (font != null) {
            specCaptcha.setFont(font);
        }

        out((Captcha)specCaptcha, request, response);
    }

    public static void out(Captcha captcha, HttpServletRequest request, HttpServletResponse response) throws IOException {
        setHeader(response);
        request.getSession().setAttribute(SESSION_KEY, captcha.text().toLowerCase());
        captcha.out(response.getOutputStream());
    }

    public static boolean ver(String code, HttpServletRequest request) {
        if (code != null) {
            String captcha = (String)request.getSession().getAttribute("captcha");
            return code.trim().toLowerCase().equals(captcha);
        } else {
            return false;
        }
    }

    public static void clear(HttpServletRequest request) {
        request.getSession().removeAttribute(SESSION_KEY);
    }

    public static void setHeader(HttpServletResponse response) {
        response.setContentType("image/gif");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0L);
    }
    
    public static void main(String[] args) {
		System.out.println("112345\n464887".split("\n")[0]);
	}
}