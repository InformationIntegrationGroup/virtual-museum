package scalar.entity;

import java.util.HashSet;



/**
 * Users generated by MyEclipse - Hibernate Tools
 */
public class Person  {

    // Constructors
	private String URI; //id/person-institution/number
	private String id;
	
	private String displayName; //name
	private String displayNameURI;
	
	
	private String mainRepresentationURI;//image

	private String birthURI;
	private String deathURI;
	private String birthPlace;
	private String deathPlace;
	private String deathPlaceURI;
	private String birthPlaceURI;
	private String birthDate;
	private String deathDate;
	
	private String nationalityURI;
	private String nationality;
	
	
	private HashSet<String> associateEventURISet;
    private HashSet<String> associatePlaceSet;
	private HashSet<String> associatePlaceURISet;
	
	private String artistBio;
	private String primaryArtistBio;
	
	private String dbpediaURI;
	private String nytimesURI;
	private String fbaseURI;
	private String wikipediaURI;

	private HashSet<Work> collectionList;
	private String scalarURI;
	
    /** default constructor */
    public Person() {
    }
    
    public Person(String personURI)
    {
    	this.URI=personURI;
    }


    public Person(String uri, String id, String displayName, String displayNameURI, String mainRepresentationURI, String birthURI, String deathURI, String birthPlace, String deathPlace, String birthDate, String deathDate, String nationalityURI, String nationality, HashSet<String> associateEventURISet, HashSet<String> associatePlaceURI,HashSet<String> associatePlaceSet,String artistBio, String primaryArtistBio, String dbpediaURI, String nytimesURI, String fbaseURI, String wikipediaURI,HashSet<Work> collectionList) {
		super();
		this.URI = uri;
		this.id=id;
		this.displayName = displayName;
		this.displayNameURI = displayNameURI;
		this.mainRepresentationURI = mainRepresentationURI;
		this.birthURI = birthURI;
		this.deathURI = deathURI;
		this.birthPlace = birthPlace;
		this.deathPlace = deathPlace;
		this.birthDate = birthDate;
		this.deathDate = deathDate;
		this.nationalityURI = nationalityURI;
		this.nationality = nationality;
		this.associateEventURISet = associateEventURISet;
		this.associatePlaceSet=associatePlaceSet;
		this.artistBio = artistBio;
		this.primaryArtistBio = primaryArtistBio;
		this.dbpediaURI = dbpediaURI;
		this.nytimesURI = nytimesURI;
		this.wikipediaURI=wikipediaURI;
		this.fbaseURI = fbaseURI;
		
		this.collectionList=new HashSet<Work>();
    	for(Work temp:collectionList)
    		this.collectionList.add(temp);
	}



	public String getArtistBio() {
		return artistBio;
	}


	public void setArtistBio(String artistBio) {
		this.artistBio = artistBio;
	}


	

	public String getBirthDate() {
		return birthDate;
	}


	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}


	public String getBirthPlace() {
		return birthPlace;
	}


	public void setBirthPlace(String birthPlace) {
		this.birthPlace = birthPlace;
	}


	public String getBirthURI() {
		return birthURI;
	}


	public void setBirthURI(String birthURI) {
		this.birthURI = birthURI;
	}


	public HashSet<Work> getCollectionList() {
		return collectionList;
	}


	public void setCollectionList(HashSet<Work> collectionList) {
		this.collectionList=new HashSet<Work>();
		for(Work temp:collectionList)
			this.collectionList.add(temp);
		}


	public String getDbpediaURI() {
		return dbpediaURI;
	}


	public void setDbpediaURI(String dbpediaURI) {
		this.dbpediaURI = dbpediaURI;
	}


	public String getDeathDate() {
		return deathDate;
	}


	public void setDeathDate(String deathDate) {
		this.deathDate = deathDate;
	}


	public String getDeathPlace() {
		return deathPlace;
	}


	public void setDeathPlace(String deathPlace) {
		this.deathPlace = deathPlace;
	}


	public String getDeathURI() {
		return deathURI;
	}


	public void setDeathURI(String deathURI) {
		this.deathURI = deathURI;
	}


	public String getDisplayName() {
		return displayName;
	}


	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}


	public String getDisplayNameURI() {
		return displayNameURI;
	}


	public void setDisplayNameURI(String displayNameURI) {
		this.displayNameURI = displayNameURI;
	}


	public String getFbaseURI() {
		return fbaseURI;
	}


	public void setFbaseURI(String fbaseURI) {
		this.fbaseURI = fbaseURI;
	}


	public String getMainRepresentationURI() {
		return mainRepresentationURI;
	}


	public void setMainRepresentationURI(String mainRepresentationURI) {
		this.mainRepresentationURI = mainRepresentationURI;
	}


	public String getNationality() {
		return nationality;
	}


	public void setNationality(String nationality) {
		this.nationality = nationality;
	}


	public String getNationalityURI() {
		return nationalityURI;
	}


	public void setNationalityURI(String nationalityURI) {
		this.nationalityURI = nationalityURI;
	}


	public String getNytimesURI() {
		return nytimesURI;
	}


	public void setNytimesURI(String nytimesURI) {
		this.nytimesURI = nytimesURI;
	}


	public String getPrimaryArtistBio() {
		return primaryArtistBio;
	}


	public void setPrimaryArtistBio(String primaryArtistBio) {
		this.primaryArtistBio = primaryArtistBio;
	}


	public String getURI() {
		return URI;
	}


	public void setURI(String uri) {
		URI = uri;
	}



	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getWikipediaURI() {
		return wikipediaURI;
	}


	public void setWikipediaURI(String wikipediaURI) {
		this.wikipediaURI = wikipediaURI;
	}

	public String getScalarURI() {
		return scalarURI;
	}

	public void setScalarURI(String scalarURI) {
		this.scalarURI = scalarURI;
	}

	public String getDeathPlaceURI() {
		return deathPlaceURI;
	}

	public void setDeathPlaceURI(String deathPlaceURI) {
		this.deathPlaceURI = deathPlaceURI;
	}

	public String getBirthPlaceURI() {
		return birthPlaceURI;
	}

	public void setBirthPlaceURI(String birthPlaceURI) {
		this.birthPlaceURI = birthPlaceURI;
	}

	public HashSet<String> getAssociateEventURISet() {
		return associateEventURISet;
	}
	
	public void addAssociateEventURI(String associateEventURI)
	{
		if(associateEventURISet==null)
			associateEventURISet=new HashSet<String>();
		associateEventURISet.add(associateEventURI);
	}

	public void setAssociateEventURISet(HashSet<String> associateEventURISet) {
		this.associateEventURISet = associateEventURISet;
	}

	public HashSet<String> getAssociatePlaceSet() {
		return associatePlaceSet;
	}
	
	public void addAssociatePlace(String associatePlace)
	{
		if(associatePlaceSet==null)
			associatePlaceSet=new HashSet<String>();
		associatePlaceSet.add(associatePlace);
	}

	public void setAssociatePlaceSet(HashSet<String> associatePlaceSet) {
		this.associatePlaceSet = associatePlaceSet;
	}

	public HashSet<String> getAssociatePlaceURISet() {
		return associatePlaceURISet;
	}
	
	public void addAssociatePlaceURI(String associatePlaceURI)
	{
		if(associatePlaceURISet==null)
			associatePlaceURISet=new HashSet<String>();
		associatePlaceURISet.add(associatePlaceURI);
	}


	public void setAssociatePlaceURISet(HashSet<String> associatePlaceURISet) {
		this.associatePlaceURISet = associatePlaceURISet;
	}
	
}
