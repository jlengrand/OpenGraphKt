#Scrape test module

The scrape test module is intended to test the immplementation of the library at scale by parsing a large amount of webpages and checking the quality of its results

## Data

At this moment

* one dataset was found on [Kaggle](https://www.kaggle.com/datasets/hetulmehta/website-classification).
* another on [Moz](https://moz.com/top-500/download/?table=top500Domains) (Top 500 most visited websites).

I'd like a more varied set of data from different types of sources, and the current set mostly seem to contain homepages but it's surprisingly hard to find.

## Running the tests

For various reasons, I am not uploading the actual data of the various URLs. To run the analysis yourself:

1. Run `Scraper.kt` once, which will grab all the webpages and place them in the `data/web` folder.
2. Run `ParserTest.kt`, which will run the `Parser` on each of those web pages and check whether the tags can be extracted, and if the page is considered valid.

