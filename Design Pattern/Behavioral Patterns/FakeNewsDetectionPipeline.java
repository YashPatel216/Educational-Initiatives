// Handler interface
interface NewsHandler {
    void setNext(NewsHandler nextHandler);
    void handle(NewsArticle article);
}

// Request class
class NewsArticle {
    String title;
    String content;
    String source;

    public NewsArticle(String title, String content, String source) {
        this.title = title;
        this.content = content;
        this.source = source;
    }
}

// Concrete Handlers
class FactCheckHandler implements NewsHandler {
    private NewsHandler next;

    @Override
    public void setNext(NewsHandler nextHandler) {
        this.next = nextHandler;
    }

    @Override
    public void handle(NewsArticle article) {
        if(article.content.toLowerCase().contains("earth is flat")) {
            System.out.println("[FactCheck]  Fake claim detected: 'Earth is flat'");
            return; // Stop chain
        }
        System.out.println("[FactCheck]  Passed fact check");
        if(next != null) next.handle(article);
    }
}

class ToxicityHandler implements NewsHandler {
    private NewsHandler next;

    @Override
    public void setNext(NewsHandler nextHandler) {
        this.next = nextHandler;
    }

    @Override
    public void handle(NewsArticle article) {
        if(article.content.toLowerCase().contains("stupid") || article.content.toLowerCase().contains("idiot")) {
            System.out.println("[ToxicityCheck]  Toxic language detected");
            return;
        }
        System.out.println("[ToxicityCheck]  No toxic language");
        if(next != null) next.handle(article);
    }
}

class BiasHandler implements NewsHandler {
    private NewsHandler next;

    @Override
    public void setNext(NewsHandler nextHandler) {
        this.next = nextHandler;
    }

    @Override
    public void handle(NewsArticle article) {
        if(article.content.toLowerCase().contains("only our party can save the world")) {
            System.out.println("[BiasCheck]  Political bias detected");
            return;
        }
        System.out.println("[BiasCheck]  Neutral content");
        if(next != null) next.handle(article);
    }
}

class SourceCredibilityHandler implements NewsHandler {
    private NewsHandler next;

    @Override
    public void setNext(NewsHandler nextHandler) {
        this.next = nextHandler;
    }

    @Override
    public void handle(NewsArticle article) {
        if(article.source.equalsIgnoreCase("randomblog.com")) {
            System.out.println("[SourceCredibility]  Unreliable source detected");
            return;
        }
        System.out.println("[SourceCredibility]  Trusted source");
        if(next != null) next.handle(article);
    }
}

// Client
public class FakeNewsDetectionPipeline {
    public static void main(String[] args) {
        // Create handlers
        NewsHandler factCheck = new FactCheckHandler();
        NewsHandler toxicity = new ToxicityHandler();
        NewsHandler bias = new BiasHandler();
        NewsHandler credibility = new SourceCredibilityHandler();

        // Build chain: FactCheck → Toxicity → Bias → Credibility
        factCheck.setNext(toxicity);
        toxicity.setNext(bias);
        bias.setNext(credibility);

        // Example articles
        NewsArticle article1 = new NewsArticle(
                "Shocking Discovery!",
                "The Earth is flat and scientists are hiding it.",
                "trustednews.com"
        );

        NewsArticle article2 = new NewsArticle(
                "Election Update",
                "Only our party can save the world! The others are stupid.",
                "randomblog.com"
        );

        NewsArticle article3 = new NewsArticle(
                "Tech Innovation",
                "AI is helping doctors detect cancer early.",
                "bbc.com"
        );

        System.out.println("---- Analyzing Article 1 ----");
        factCheck.handle(article1);

        System.out.println("\n---- Analyzing Article 2 ----");
        factCheck.handle(article2);

        System.out.println("\n---- Analyzing Article 3 ----");
        factCheck.handle(article3);
    }
}

