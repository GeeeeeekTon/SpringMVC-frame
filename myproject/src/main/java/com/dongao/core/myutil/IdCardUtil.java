package com.dongao.core.myutil;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 身份证号 工具类 
 * @author zhaominglei
 *
 */
public class IdCardUtil { 
	private static IdCardUtil instance = null;
	private IdCardUtil(){ 

    }
	public static IdCardUtil getInstance() {
		if (instance == null) {
			instance = new IdCardUtil();
		}
		return instance;
	}
    
    /** 
     * 15位身份证号码转化为18位的身份证。如果是18位的身份证则直接返回，不作任何变化。 
     * @param idCard,15位的有效身份证号码 
     * @return idCard18 返回18位的有效身份证 
     */ 
    public String IdCard15to18(String idCard){ 
    	if (StringUtil.isEmptyString(idCard)) {
    		return "";
    	}
        idCard = idCard.trim(); 
        StringBuffer idCard18 = new StringBuffer(idCard); 
        //加权因子 
        //int[] weight = {7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2}; 
        //校验码值 
        char[] checkBit = {'1','0','X','9','8','7','6','5','4','3','2'}; 
        int sum = 0; 
        //15位的身份证 
        if(idCard != null && idCard.length()==15){ 
            idCard18.insert(6, "19"); 
            for(int index=0;index<idCard18.length();index++){ 
                char c = idCard18.charAt(index); 
                int ai = Integer.parseInt(new Character(c).toString()); 
                //sum = sum+ai*weight[index]; 
                //加权因子的算法 
                int Wi = ((int)Math.pow(2, idCard18.length()-index))%11; 
                sum = sum+ai*Wi; 
            } 
            int indexOfCheckBit = sum%11; //取模 
            idCard18.append(checkBit[indexOfCheckBit]); 
        } 

        return idCard18.toString(); 
    } 

    /** 
     * 转化18位身份证位15位身份证。如果输入的是15位的身份证则不做任何转化，直接返回。 
     * @param idCard 18位身份证号码 
     * @return idCard15 
     */ 
    public String IdCard18to15(String idCard){ 
    	if (StringUtil.isEmptyString(idCard)) {
    		return "";
    	}
        idCard = idCard.trim(); 
        StringBuffer idCard15 = new StringBuffer(idCard); 
        if(idCard!=null && idCard.length()==18){ 
            idCard15.delete(17, 18); 
            idCard15.delete(6,8); 
        } 
        return idCard15.toString(); 

    } 

    /** 
     * 校验是否是一个有效的身份证。如果是18的身份证，则校验18位的身份证。15位的身份证不校验，也无法校验 
     * @param idCart 
     * @return 
     */ 
    public boolean checkIDCard(String idCard){
    	if (StringUtil.isEmptyString(idCard)) {
    		return false;
    	}
        boolean isIDCard = false; 
        Pattern pattern = Pattern.compile("\\d{15}|\\d{17}[x,X,0-9]"); 
        Matcher matcher = pattern.matcher(idCard); 
        if(matcher.matches()){//可能是一个身份证 
            isIDCard = true; 
            if(idCard.length()==18){//如果是18的身份证，则校验18位的身份证。15位的身份证暂不校验 
                String IdCard15 = IdCard18to15(idCard); 
                String IdCard18 = IdCard15to18(IdCard15); 
                if(!idCard.equalsIgnoreCase(IdCard18)){ 
                    isIDCard = false; 
                } 
            }else if(idCard.length()==15){ 
                isIDCard = true; 
            }else{ 
                isIDCard = false; 
            } 
        } 

        return isIDCard; 
    }
    
    /** 
     * 校验是否是一个有效的香港身份证码
     * @param idCard
     * @return 
     */ 
    public boolean checkHongKongIDCard(String idCard){ 
    	if (StringUtil.isEmptyString(idCard)) {
    		return false;
    	}
        boolean isIDCard = false;
        Pattern pattern = Pattern.compile("^[a-zA-Z]{1,2}[0-9]{6}\\(?[0-9a-zAZ-Z]\\)?$"); 
        Matcher matcher = pattern.matcher(idCard); 
        if (matcher.matches()) {
            if (validateHKCard(idCard)) {
            	isIDCard = true; 
            }
        }
        return isIDCard; 
    }
    
    /**
     * 验证香港身份证号码(存在Bug，部份特殊身份证无法检查)
     * <p>
     * 身份证前2位为英文字符，如果只出现一个英文字符则表示第一位是空格，对应数字58 前2位英文字符A-Z分别对应数字10-35
     * 最后一位校验码为0-9的数字加上字符"A"，"A"代表10
     * </p>
     * <p>
     * 将身份证号码全部转换为数字，分别对应乘9-1相加的总和，整除11则证件号码有效
    * </p>
     *
     * @param idCard 身份证号码
    *  @return 验证码是否符合
    */
    public static boolean validateHKCard(String idCard) {
    	if (StringUtil.isEmptyString(idCard)) {
    		return false;
    	}
        String card = idCard.replaceAll("[\\(|\\)]", "");
        Integer sum = 0;
        if (card.length() == 9) {
            sum = (Integer.valueOf(card.substring(0, 1).toUpperCase().toCharArray()[0]) - 55) * 9
                    + (Integer.valueOf(card.substring(1, 2).toUpperCase().toCharArray()[0]) - 55) * 8;
            card = card.substring(1, 9);
        } else {
            sum = 522 + (Integer.valueOf(card.substring(0, 1).toUpperCase().toCharArray()[0]) - 55) * 8;
        }
        String mid = card.substring(1, 7);
        String end = card.substring(7, 8);
        char[] chars = mid.toCharArray();
        Integer iflag = 7;
        for (char c : chars) {
            sum = sum + Integer.valueOf(c + "") * iflag;
            iflag--;
        }
        if (end.toUpperCase().equals("A")) {
            sum = sum + 10;
        } else {
            sum = sum + Integer.valueOf(end);
        }
        return (sum % 11 == 0) ? true : false;
    }
    public static void main(String[] args) {
//		System.out.println("392701198302059225".length());
		System.out.println(IdCardUtil.getInstance().IdCard18to15("140203196705202023"));
		System.out.println(IdCardUtil.getInstance().checkIDCard("440301197904163643"));
	}
}