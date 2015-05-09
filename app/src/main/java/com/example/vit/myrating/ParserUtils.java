package com.example.vit.myrating;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Vit on 09.05.2015.
 */
public class ParserUtils {

    static final String TAG = "myrating";
    static final String CLASS = "Parser: ";

    // <td> position in html <table>
    static final int SUBJECT_TITLE_ID = 0;
    static final int SUBJECT_TYPE_ID = 1;
    static final int SUBJECT_TOTAL_MARK_ID = 20;
    static final int SUBJECT_FIRST_MODULE_ID = 2;
    static final int SUBJECT_LAST_MODULE_ID = 19;
    static final int NUM_OF_MODULE_FIELDS = 3;

    //input argument page - html page downloaded from wev
    public static List<Subject> parsePage(String page) {

        Document doc = Jsoup.parse(page);

        int semester = 1;

        List<Subject> subjects = new ArrayList<>();

        //get tables with rating by tag <tbody>
        Elements tables = doc.getElementsByTag("tbody");
        //check every table
        for (Element table : tables) {

            // if there are less than 2 rows in table then the table
            // hasn't subjects
            if (table.getElementsByTag("tr").size() < 2)
                continue;

            for (Element row : table.select("tr")) {

                //Create array list of modules of current subject
                List<Subject.Module> modules = new ArrayList<Subject.Module>();
                // first fill the module array list
                for (int i = SUBJECT_FIRST_MODULE_ID; i <= SUBJECT_LAST_MODULE_ID; i += NUM_OF_MODULE_FIELDS) {
                    // position:
                    // i = module percentage
                    // i+1 = module date
                    // i+2 = module mark

                    // if <td> with percentage is empty then no such module
                    if (row.child(i).text() == "") {
                        continue;
                    }

                    //Log.d(TAG, CLASS + "module percentage - " + row.child(i).text());
                    //Log.d(TAG, CLASS + "module date - " + row.child(i + 1).text());
                    //Log.d(TAG, CLASS + "module mark - " + row.child(i + 2).text());

                    modules.add(new Subject.Module(
                            row.child(i).text()
                            , row.child(i + 1).text()
                            , row.child(i + 2).text()
                    ));

                }
                //Log.d(TAG, CLASS + " subject title - " + row.child(SUBJECT_TITLE_ID).text());
                //Log.d(TAG, CLASS + " subject type - " + row.child(SUBJECT_TYPE_ID).text());
                //Log.d(TAG, CLASS + " subject total mark - " + row.child(SUBJECT_TOTAL_MARK_ID).text());

                //put current subject to the subjects array list
                subjects.add(
                        new Subject(
                                row.child(SUBJECT_TITLE_ID).text()
                                ,row.child(SUBJECT_TYPE_ID).text()
                                ,row.child(SUBJECT_TOTAL_MARK_ID).text()
                                ,semester
                                ,modules
                        )
                );
            }
            // go to the table of next semester
            semester++;

        }

        /*
        for(Subject subject : subjects){
            subject.print();
        }
        */
        return subjects;
    }
}
