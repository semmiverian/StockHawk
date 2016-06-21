package com.sam_chordas.android.stockhawk.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Semmiverian on 6/18/16.
 */
public class Stock {
    private String name;
    @SerializedName("query")
    private Query query;

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    public Stock() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public class Query {
        private String count;
        @SerializedName("results")
        private Result result;

        public Result getResult() {
            return result;
        }

        public void setResult(Result result) {
            this.result = result;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }
    }


    public class Result{
        private Quote quote;

        public Result() {
        }





        public Quote getQuote() {
            return quote;
        }

        public void setQuote(Quote quote) {
            this.quote = quote;
        }
    }

    public class Quote{

        @SerializedName("Name")
        private String Name;
        @SerializedName("Symbol")
        private String symbol;
        @SerializedName("Open")
        private String open;

        public Quote() {
        }
        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public String getOpen() {
            return open;
        }

        public void setOpen(String open) {
            this.open = open;
        }
    }


}
