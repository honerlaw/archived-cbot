package org.cbot.ocr;

public class CharacterDataMap {
	
	private String character;
	private OCRImage image;
	
	public CharacterDataMap(String character) {
		this.character = character;
	}
	
	public CharacterDataMap(String character, OCRImage image) {
		this.character = character;
		this.image = image;
	}
	
	public CharacterDataMap(OCRImage image) {
		this.image = image;
	}
	
	public String getCharacter() {
		return character;
	}
	
	public OCRImage getImage() {
		return image;
	}
	
	public void setCharacter(String character) {
		this.character = character;
	}
	
	public boolean equal(CharacterDataMap other, double tolerance) {
		if(getImage().getData().length != other.getImage().getData().length)
			return false;
		for(int i = 0; i < getImage().getData().length; i++) {
			if(!getImage().isInTolerance(other.getImage().getData()[i], getImage().getData()[i], tolerance))
				return false;
		}
		return true;
	}

}
