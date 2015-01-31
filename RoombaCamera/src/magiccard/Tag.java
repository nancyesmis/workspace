package magiccard;

public class Tag {
    public static enum Type {
        Object("Object"),
        Action("Action"),
        Compound("Compound"),
        Time("Time"),
        Location("Location"),
        Preposition("Preposition"),
        Order("Order"),
        Exception("Exception");

        private String name;

        private Type(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }
    }


    public int id;
    public Type type;
    public String name;
    public int found;
    public int miss;
    public Vector2D position;
    public Vector2D orientation;
    double size;
    
    /**
     * We need to be able to save the tags onto a file, since we have already built parsers for
     * the original text, we will use that as the format for us to save the state of the tags
     * This will be useful for creating memorized tasks  
     */
    private String originalTextFromVisionServer;

    
	public Tag() {
        id = -1;
        name = "";
        found = 0; 
        miss = 0;        
        position = new Vector2D();
        orientation = new Vector2D();
        size = 0.0;
    }
	

	public Tag(Type type,int id, String name, int found, Vector2D position, Vector2D orientation ) {
        this.id = id;
        this.name = name;
        this.found = found;
        this.position = position;
        this.orientation = orientation;
        this.type =type;
       
    }
	
	
	public String getOriginalText() {
		return originalTextFromVisionServer;
	}

	public void setOriginalText(String originalText) {
		this.originalTextFromVisionServer = originalText;
	}
	public void setPosition(Vector2D myPosition) {
		/*this.position.x = myPosition.x;
		this.position.y = myPosition.y;*/
		this.position = new Vector2D(myPosition);
	}

    public String toString() {
        if (found > 0)
            return name + " (id:" + id + " type:" + type.toString() + " found:"+ found+ " position:" +
                position.toString() + " orientation:" + orientation.toString();
        else
            return "not found";
    }
}
