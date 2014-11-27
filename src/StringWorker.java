import java.util.ArrayList;


class StringWorker {
	static ArrayList<String> convertOutput(ArrayList<String> output){
		try{
			ArrayList<String> result=new ArrayList<String>();
			String curRes="";
			for (int i=0;;){
				curRes=curRes+output.get(i)+" ";
				if (i==output.size()-1) {
					curRes=curRes+output.get(i);
					result.add(curRes);
					break;
			
				}
	//			if (i!=output.size()-1){
					if ((output.get(i+1).startsWith("n"))) {
						result.add(curRes);
						curRes="";					
						}
	//			}
				
			i++;	
			}
			return result;
		} 
		catch (Exception e)	{
			e.printStackTrace();
			return null;
		}
	}
	public static void main(String[] args) {
		ArrayList<String> output=new ArrayList<String>();
		output.add("n/name1");
		output.add("data1");
		output.add("data2");
		output.add("n/name2");
		output.add("data3");
		output.add("data4");
		ArrayList<String> result=convertOutput(output);
		for (int i=0;i<result.size();i++){
			System.out.println(result.get(i));
		}

	}

}
