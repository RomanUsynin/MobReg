package com.romanusynin.mobreg.mobreg;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class Parser {

    public static ArrayList<Region> getRegions() {
        try {
            Document doc = Jsoup.connect(Constants.REGIONS_URL).get();
            ArrayList<Region> regionsArrayList = new ArrayList<Region>();
            Elements regionsElements = doc.select("#city_list").get(0).select("li");
            for (int i=2; i<regionsElements.size(); i++)
            {
                String url = regionsElements.get(i).select("li").get(0).select("a").get(0).attr("href");
                String name = regionsElements.get(i).select("li").get(0).select("a").get(0).select("span").get(0).childNodes().get(0).toString();
                String countHospital = regionsElements.get(i).select("li").get(0).select("a").get(0).select("span").get(0).childNodes().get(1).childNode(0).toString();
                Region region = new Region(name, url, countHospital);
                regionsArrayList.add(region);
            }
            return regionsArrayList;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<Hospital> getHospitals(String urlRegion) {
        try {
            Document doc = Jsoup.connect(Constants.DOMAIN+urlRegion).get();
            ArrayList<Hospital> hospitalsArrayList = new ArrayList<Hospital>();
            Elements hospitalsElements = doc.select("#pol_list").get(0).select("li");
            for (int i=0;i<hospitalsElements.size(); i++){
                if (hospitalsElements.get(i).select("ul").size() == 0){
                    String url = hospitalsElements.get(i).select("a").get(0).attr("href");
                    String name = hospitalsElements.get(i).select("span").get(0).childNode(0).toString();
                    String address = hospitalsElements.get(i).select("span").get(1).text();
                    Hospital hospital = new Hospital(name, url, address);
                    hospitalsArrayList.add(hospital);
                }
                else{
                    continue;
                }
            }
            return hospitalsArrayList;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<Department> getDepartments(String urlHospital) {
        try {
            Document doc = Jsoup.connect(Constants.DOMAIN+urlHospital).get();
            ArrayList<Department> departmentArrayList = new ArrayList<Department>();
            Elements departmentsElements = doc.select("#spec_list").get(0).select("li");
            for (int i=0;i<departmentsElements.size(); i+=2){
                String url = departmentsElements.get(i+1).select("a").get(0).attr("href");
                String name = departmentsElements.get(i).select("span").get(0).childNode(2).toString().substring(1);
                String countTickets = departmentsElements.get(i).select("span").get(2).text();
                Department department = new Department(name, url, countTickets );
                departmentArrayList.add(department);
            }
            return departmentArrayList;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getHospitalPhone(String urlHospital){
        try {
            Document doc = Jsoup.connect(Constants.DOMAIN + urlHospital).get();
            if (doc.select(".explanation_step").get(0).childNodes().size() == 1 || doc.select(".explanation_step").get(0).childNode(2).toString().length() == 1) {
                return null;
            }
            return doc.select(".explanation_step").get(0).childNode(2).toString().split(" ")[2];
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static ArrayList<Doctor> getDoctors(String urlDepartment){
        try {
            Document doc = Jsoup.connect(Constants.DOMAIN + urlDepartment).get();
            ArrayList<Doctor> doctorsArrayList = new ArrayList<Doctor>();
            Elements doctorsElements = doc.select(".table_week");

        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
}
