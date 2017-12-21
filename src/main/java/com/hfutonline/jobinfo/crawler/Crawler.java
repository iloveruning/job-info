package com.hfutonline.jobinfo.crawler;


import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTableBody;
import com.hfutonline.jobinfo.entity.Job;
import com.hfutonline.jobinfo.mapper.JobMapper;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.util.Iterator;

/**
 * @author chenliangliang
 * @date 2017/12/11
 */
public class Crawler {


    private WebClient client;

    private final String initUrl;

    private JobMapper jobMapper;

    private final Logger logger = LoggerFactory.getLogger(Crawler.class);

    private int update;

    private int insert;


    public Crawler(JobMapper jobMapper) {

        update=0;
        insert=0;

        client = new WebClient(BrowserVersion.CHROME);
        client.getOptions().setCssEnabled(false);
        client.getOptions().setDownloadImages(false);
        client.getOptions().setJavaScriptEnabled(false);
        client.getOptions().setTimeout(10000);
        client.getOptions().setThrowExceptionOnScriptError(false);
        client.getOptions().setActiveXNative(false);
        client.getOptions().setAppletEnabled(false);
        this.jobMapper=jobMapper;
        this.initUrl = "http://gdjy.hfut.edu.cn/products/list/1.html?list=a&page=1&per-page=18";

    }


    public void run() throws Exception{
        HtmlPage page = client.getPage(initUrl);

        HtmlElement element = page.getFirstByXPath("//*[@id=\"w0\"]/div/b[2]");
        String total = element.getTextContent();
        int all = Integer.parseInt(total);
        int tol = jobMapper.count();
        int d = all - tol;
        int pageNum = d / 18 + 1;
        for (int i = 1; i <= pageNum; i++) {

            String url = "http://gdjy.hfut.edu.cn/products/list/1.html?list=a&page=" + i + "&per-page=18";
            if (i != 1) {
                page = client.getPage(url);
            }

            HtmlTableBody table = page.getFirstByXPath("//*[@id=\"w0\"]/table/tbody");

            Iterable<DomNode> children = table.getChildren();

            for (DomNode c : children) {
                NamedNodeMap map = c.getAttributes();
                Node key = map.getNamedItem("data-key");
                if (key != null) {
                    String id = key.getNodeValue();
                    String url1 = "http://gdjy.hfut.edu.cn/products/" + id + ".html";

                    logger.info(url1);
                    Iterator<DomNode> ch = c.getChildren().iterator();
                    Job job = new Job();
                    int temp = 0;
                    while (ch.hasNext()) {
                        String text = ch.next().getTextContent();
                        switch (temp) {
                            case 0:
                                job.setCompany(text);
                                break;
                            case 1:
                                text = timeFormat(text);
                                job.setTime(text);
                                break;
                            case 2:
                                job.setPlace(text);
                                break;
                            case 3:
                                job.setClick(text);
                                break;
                            default:
                                break;
                        }
                        ++temp;
                    }
                    job.setId(id);
                    if (jobMapper.isExist(id) == 1) {
                        int re = jobMapper.updateClick(job.getClick(), id);
                        if (re == 1) {
                            update++;
                            logger.info(id + ".html 更新成功！");
                        } else {
                            logger.info(id + ".html 更新失败！");
                        }
                    } else {
                        Flowable.fromCallable(() -> {
                            HtmlPage infoPage = client.getPage(url1);
                            HtmlElement ele = infoPage.getFirstByXPath("/html/body/div/div[2]/div/div/div/div[1]/div[3]");
                            //对图片进行处理
                            ele.getElementsByTagName("img").forEach(img -> {
                                String src = img.getAttribute("src");
                                if (!src.startsWith("http")) {
                                    src = "http://gdjy.hfut.edu.cn" + src;
                                }
                                logger.info("img: " + src);
                                img.setAttribute("src", src);
                            });
                            String info = ele.asXml();
                            job.setInfo(info);

                            int res = jobMapper.save(job);
                            if (res == 1) {
                                insert++;
                                return id + ".html 保存成功！";
                            } else {
                                return id + ".html 保存失败！";
                            }

                        }).subscribeOn(Schedulers.io())
                                .observeOn(Schedulers.single())
                                .subscribe(System.out::println, Throwable::printStackTrace);
                    }
                    System.out.println("=========================");
                    try {
                        Thread.sleep(200);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
        client.close();
    }


    /**
     * 日期格式化
     *
     * @param time
     * @return
     */
    private String timeFormat(String time) {
        System.out.println(time);
        String date, hour, minute;
        String[] s1 = StringUtils.split(time, " ");
        if (s1.length == 1) {
            s1 = StringUtils.split(time, " ");
        }
        date = s1[0];
        String[] s2 = StringUtils.split(s1[1], ":");

        if (s2.length == 1) {
            s2 = StringUtils.split(s1[1], "：");
        }
        hour = s2[0];
        if (hour.length() == 1) {
            hour = "0" + hour;
        }
        minute = s2[1];
        if (minute.length() > 2) {
            minute = minute.substring(0, 2);
        }
        return date + " " + hour + ":" + minute + ":00";
    }

    public int getUpdate() {
        return update;
    }

    public int getInsert() {
        return insert;
    }

    public void close() {
        if (client != null) {
            client.close();
        }
    }


}
