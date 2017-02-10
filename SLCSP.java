package slcsp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SLCSP {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SLCSP slcsp = new SLCSP();
		Zips zips = new Zips();
		Plans plans = new Plans();
		
		slcsp.readSLCSP("slcsp.csv");
		zips.readZips("zips.csv");
		plans.readPlans("plans.csv");

		try {
		    FileWriter writer = new FileWriter(slcsp.file);
		    
		    writer.append("zipcode,rate\n");
		    
		    for (SLCSP zip : slcsp.lstZipCodes) {
		    	if(!zips.zipRemoval.contains(zip.strZipCode)){
			        Zips objZips = zips.hZips.get(zip.strZipCode);
			        Plans objPlans = plans.hPlans.get(objZips.State + objZips.RateArea);
			        if(objPlans != null){
			        	writer.append(zip.strZipCode + "," + objPlans.SecondLowestRate + "\n");
			        }
			        else{
			        	writer.append(zip.strZipCode + ",\n");
			        }
		    	}
		    	else{
		        	writer.append(zip.strZipCode + ",\n");
		    	}
		    }
		    
	        writer.flush();
	        writer.close();
		}
		catch(IOException ex){
			
		}
	}
}

class SLCSP extends Reader{
	public String strZipCode;
	public Double dlRate;
	public List<SLCSP> lstZipCodes;
	
	public List<SLCSP> readSLCSP(String fileName){
		lstZipCodes = new ArrayList<SLCSP>();
		
		readFile(fileName);
		
		try{
			reader.readLine();
		    while ((text = reader.readLine()) != null) {
		    	SLCSP elemSLCSP = new SLCSP();
		    	text = text.substring(0, text.length() - 1);
		    	
		    	elemSLCSP.strZipCode = text;
		    	lstZipCodes.add(elemSLCSP);
		    }
		}
		catch(IOException ex){
			
		}
		
	    return lstZipCodes;
	}
}
class Zips extends Reader{
	public String ZipCode;
	public String State;
	public String CountyCode;
	public String CountyName;
	public Integer RateArea;
	Map<String, Zips> hZips;
	List<String> zipRemoval;
	
	public Map<String, Zips> readZips(String fileName){
		hZips = new HashMap<String, Zips>();
		zipRemoval = new ArrayList<String>();
		
		readFile(fileName);
		
		try{
			reader.readLine();
		    while ((text = reader.readLine()) != null) {
		    	Zips mapZips = new Zips();
		    	String[] array = text.split(",");
		    	
		    	mapZips.ZipCode = array[0];
		    	mapZips.State = array[1];
		    	mapZips.CountyCode = array[2];
		    	mapZips.CountyName = array[3];
		    	mapZips.RateArea = Integer.parseInt(array[4]);
		    	
		        Zips objZips = hZips.get(mapZips.ZipCode);
		        
		        if(objZips != null){
			        if(objZips.State.equals(mapZips.State) && objZips.RateArea == mapZips.RateArea){
			        	hZips.put(mapZips.ZipCode, mapZips);	
			        }
			        else{
			        	zipRemoval.add(mapZips.ZipCode);
			        }
		        }
		        else{
		        	hZips.put(mapZips.ZipCode, mapZips);	
		        }
		    }
		}
		catch(IOException ex){
			
		}
		
	    return hZips;
	}
}
class Plans extends Reader{
	public String PlanId;
	public String State;
	public String MetalLevel;
	public Double SecondLowestRate;
	public Double LowestRate;	
	public Integer RateArea;
	Map<String, Plans> hPlans = new HashMap<String, Plans>();
	
	public Map<String, Plans> readPlans(String fileName){
		hPlans = new HashMap<String, Plans>();
		
		readFile(fileName);
		
		try{
			reader.readLine();
		    while ((text = reader.readLine()) != null) {
		    	Plans mapPlans = new Plans();
		    	String[] array = text.split(",");
		    	
		    	mapPlans.PlanId = array[0];
		    	mapPlans.State = array[1];
		    	mapPlans.MetalLevel = array[2];
		    	
		    	mapPlans.LowestRate = Double.parseDouble(array[3]);
		    	mapPlans.SecondLowestRate = Double.parseDouble(array[3]);
		    	mapPlans.RateArea = Integer.parseInt(array[4]);
		    	
		        Plans objPlans = hPlans.get(mapPlans.State + mapPlans.RateArea);
		        
		        if(mapPlans.MetalLevel.contains("Silver")){
			        if(objPlans != null){
			        	if(mapPlans.LowestRate < objPlans.LowestRate){
				        	mapPlans.SecondLowestRate = objPlans.LowestRate;	
				        	hPlans.put(mapPlans.State + mapPlans.RateArea, mapPlans);
			        	}
			        	else if(mapPlans.SecondLowestRate < objPlans.SecondLowestRate){
				        	mapPlans.LowestRate = objPlans.LowestRate;			        		
				        	hPlans.put(mapPlans.State + mapPlans.RateArea, mapPlans);
			        	}
			        }
			        else{
			        	hPlans.put(mapPlans.State + mapPlans.RateArea, mapPlans);
			        }
		        }
		    }
		}
		catch(IOException ex){
			
		}
		
	    return hPlans;
	}
}

class Reader{
	BufferedReader reader = null;
	File file = null;
	String text = null;
	
	
	public File createFile(String fileName){
		file = new File(fileName);
		return file;
	}
	
	public void readFile(String fileName){
		try{
			reader = new BufferedReader(new FileReader(createFile(fileName)));
		}
		catch(IOException ex){
			
		}
	}
}
