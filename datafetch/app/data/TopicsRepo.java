package data;

import analytics.Tokenize;
import com.avaje.ebean.Ebean;
import com.avaje.ebean.Query;
import com.avaje.ebean.RawSql;
import com.avaje.ebean.RawSqlBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import helper.Counter;
import helper.DatetimeHelper;
import helper.JsonHelper;
import helper.TweetWrapper;
import models.Handle;
import models.Topics;
import twitter4j.Status;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TopicsRepo {

    public static final String text = "^[a-zA-Z]+$";
    static Pattern textPattern = Pattern.compile(text, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    static String[] stop = new String[]{"i","I","lol","a","able","about","above","abroad","abst","accordance","according","accordingly","across","act","actually","added","adj","adopted","affected","affecting","affects","after","afterwards","again","against","ago","ah","ahead","ain't","all","allow","allows","almost","alone","along","alongside","already","also","although","always","am","amid","amidst","among","amongst","amoungst","amount","an","and","announce","another","any","anybody","anyhow","anymore","anyone","anything","anyway","anyways","anywhere","apart","apparently","appear","appreciate","appropriate","approximately","are","aren","aren't","arent","arise","around","a's","as","aside","ask","asking","associated","at","auth","available","away","awfully","b","back","backward","backwards","be","became","because","become","becomes","becoming","been","before","beforehand","begin","beginning","beginnings","begins","behind","being","believe","below","beside","besides","best","better","between","beyond","bill","biol","both","bottom","brief","briefly","but","by","c","ca","call","came","can","cannot","can't","cant","caption","cause","causes","certain","certainly","changes","clearly","c'mon","co.","co","com","come","comes","computer","con","concerning","consequently","consider","considering","contain","containing","contains","corresponding","could","couldn't","couldnt","course","cry","c's","currently","d","dare","daren't","date","de","definitely","describe","described","despite","detail","did","didn't","different","directly","do","does","doesn't","doing","done","don't","down","downwards","due","during","e","each","ed","edu","effect","eg","eight","eighty","either","eleven","else","elsewhere","empty","end","ending","enough","entirely","especially","et","et-al","etc","even","ever","evermore","every","everybody","everyone","everything","everywhere","ex","exactly","example","except","f","fairly","far","farther","few","fewer","ff","fifteen","fifth","fify","fill","find","fire","first","five","fix","followed","following","follows","for","forever","former","formerly","forth","forty","forward","found","four","from","front","full","further","furthermore","g","gave","get","gets","getting","give","given","gives","giving","go","goes","going","gone","got","gotten","greetings","h","had","hadn't","half","happens","hardly","has","hasn't","hasnt","have","haven't","having","he","he'd","hed","he'll","hello","help","hence","her","here","hereafter","hereby","herein","here's","heres","hereupon","hers","herse”","herself","he's","hes","hi","hid","him","himse”","himself","his","hither","home","hopefully","how","howbeit","however","how's","hundred","﻿I","i","i'd","id","ie","if","ignored","i'll","i'm","im","immediate","immediately","importance","important","in","inasmuch","inc.","inc","indeed","index","indicate","indicated","indicates","information","inner","inside","insofar","instead","interest","into","invention","inward","is","isn't","it","it'd","itd","it'll","it's","its","itse”","itself","i've","j","just","k","keep","keeps","kept","keys","kg","km","know","known","knows","l","largely","last","lately","later","latter","latterly","least","less","lest","let","let's","lets","like","liked","likely","likewise","line","little","'ll","look","looking","looks","low","lower","ltd","m","made","mainly","make","makes","many","may","maybe","mayn't","me","mean","means","meantime","meanwhile","merely","mg","might","mightn't","mill","million","mine","minus","miss","ml","more","moreover","most","mostly","move","mr","mrs","much","mug","must","mustn't","my","myse”","myself","n","na","hashtag","namely","nay","nd","near","nearly","necessarily","necessary","need","needn't","needs","neither","never","neverf","neverless","nevertheless","new","next","nine","ninety","no","nobody","non","none","nonetheless","no-one","noone","nor","normally","nos","not","noted","nothing","notwithstanding","novel","now","nowhere","o","obtain","obtained","obviously","of","off","often","oh","ok","okay","old","omitted","on","once","one","one's","ones","only","onto","opposite","or","ord","other","others","otherwise","ought","oughtn't","our","ours ","ours","ourselves","out","outside","over","overall","owing","own","p","page","pages","part","particular","particularly","past","per","perhaps","placed","please","plus","poorly","possible","possibly","potentially","pp","predominantly","present","presumably","previously","primarily","probably","promptly","proud","provided","provides","put","q","que","quickly","quite","qv","r","ran","rather","rd","re","readily","really","reasonably","recent","recently","ref","refs","regarding","regardless","regards","related","relatively","research","respectively","resulted","resulting","results","right","round","run","s","said","same","saw","say","saying","says","sec","second","secondly","section","see","seeing","seem","seemed","seeming","seems","seen","self","selves","sensible","sent","serious","seriously","seven","several","shall","shan't","she","she'd","shed","she'll","she's","shes","should","shouldn't","show","showed","shown","showns","shows","side","significant","significantly","similar","similarly","since","sincere","six","sixty","slightly","so","some","somebody","someday","somehow","someone","somethan","something","sometime","sometimes","somewhat","somewhere","soon","sorry","specifically","specified","specify","specifying","state","states","still","stop","strongly","sub","substantially","successfully","such","sufficiently","suggest","sup","sure","system","t","take","taken","taking","tell","ten","tends","th","than","thank","thanks","thanx","that","that'll","that's","thats","that've","the","their","theirs","them","themselves","then","thence","there","thereafter","thereby","there'd","thered","therefore","therein","there'll","thereof","there're","therere","there's","theres","thereto","thereupon","there've","these","they","they'd","theyd","they'll","they're","theyre","they've","thick","thin","thing","things","think","third","thirty","this","thorough","thoroughly","those","thou","though","thoughh","thousand","three","throug","through","throughout","thru","thus","til","till","tip","to","together","too","took","top","toward","towards","tried","tries","truly","try","trying","t's","ts","twelve","twenty","twice","two","u","un","under","underneath","undoing","unfortunately","unless","unlike","unlikely","until","unto","up","upon","ups","upwards","us","use","used","useful","usefully","usefulness","uses","using","usually","uucp","v","value","various","'ve","versus","very","via","viz","vol","vols","vs","w","want","wants","was","wasn't","way","we","we'd","wed","welcome","we'll","well","went","we're","were","weren't","we've","what","whatever","what'll","what's","whats","what've","when","whence","whenever","when's","where","whereafter","whereas","whereby","wherein","where's","wheres","whereupon","wherever","whether","which","whichever","while","whilst","whim","whither","who","who'd","whod","whoever","whole","who'll","whom","whomever","who's","whos","whose","why","why's","widely","will","willing","wish","with","within","without","wonder","won't","words","world","would","wouldn't","www","x","y","yes","yet","you","you'd","youd","you'll","your","you're","youre","yours","yourself","yourselves","you've","z","zero"};
    static Set<String> stopWords = new HashSet<>();
    static{
        for (int i = 0; i < stop.length; i++){
            stopWords.add(stop[i].toLowerCase());
        }
    }
    private static Counter counter = new Counter(10);
    public static void trendingTopics(TweetWrapper tweetWrapper, List<Integer> presentEntities) { //todo: merge trending topics and hashtags

        if (counter.incrCounter() != 0) return;
        if (!tweetWrapper.status.isRetweet()) {
            Status status = tweetWrapper.status;
            List<String> toks = Tokenize.tokenizeRawTweetText(status.getText());

            Date trimmedDate = DatetimeHelper.getDateUptoHours(status.getCreatedAt());
            for (String token: toks){
                Matcher matcher = textPattern.matcher(token.toLowerCase());
                if (!matcher.find()) continue;
                if (stopWords.contains(token.toLowerCase())) continue;

                for (Integer entityId : presentEntities) {

                    Topics topics = Ebean.find(Topics.class).where()
                            .eq("name", token)
                            .eq("entity_id", entityId)
                            .eq("created_at", trimmedDate)
                            .findUnique();
                    if (topics == null) {
                        topics = new Topics();
                        topics.count = 1;
                        topics.createdAt = trimmedDate;
                        topics.name = token;
                        topics.entityId = entityId;
                    } else {
                        topics.setCount(topics.count + 1);
                    }
                    Ebean.save(topics);
                }
            }
        }
    }

    public static JsonNode getRelevantTopics(int entityId, int maxRows){
        /*String sql = "select hashtag, sum(count) as count from topics  where entity_id = " + entityId +
        " group by hashtag";*/
        List<Topics> topics = Ebean.find(Topics.class).where().eq("entity_id", entityId).orderBy().
                desc("count").setMaxRows(maxRows).findList();

        List<TopicsAggregate> topicsAggregates = new ArrayList<>();
        for (Topics topic : topics ) {
            topicsAggregates.add(new TopicsAggregate(topic.getName(), topic.getCount()));
        }
//        RawSql rawSql2 = RawSqlBuilder.unparsed("select hashtag from  topics").columnMapping("hashtag", "hashtag").create();
//        Query<Topics> query2 = Ebean.find(Topics.class);
        //System.out.println(topics);
//        RawSql rawSql = RawSqlBuilder.parse(sql)
//                .columnMapping("hashtag", "hashtag")
//                .columnMapping("sum(count)", "count")
//                .create();
//        Query<Topics> query = Ebean.find(Topics.class);
//        query.setRawSql(rawSql);
        /*query.setRawSql(RawSqlBuilder.parse(sql).create())*//*.where().eq("entity_id", entityId)*//*
                .setMaxRows(maxRows)
                .order().desc("count")
                .findList();*/

        /*System.out.println(query + "^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^") ;
        System.out.println(query.findList());
        System.out.println(query.findSet());*/
        return JsonHelper.jsonify(topicsAggregates);
    }
}