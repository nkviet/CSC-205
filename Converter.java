import javax.swing.JOptionPane;

public class Converter {

	public static void main(String[] args) {
		String input = JOptionPane.showInputDialog("Enter a decimal: ");
		String thirtyTwoBit = convert32Bit(input);
		String sixtyFourBit = convert64Bit(input);
		float converted32 = convertBackToFloat32(thirtyTwoBit);
		float converted64 = convertBackToFloat64(sixtyFourBit);
		JOptionPane.showMessageDialog(null, "32 bit: " + thirtyTwoBit
				+ "\n Converted back to: " + converted32
				+ "\n64 bit: " + sixtyFourBit
				+ "\n Converted back to: " + converted64);

	}

	private static String convert32Bit(String input) {
		//Converted to float and separates the whole number from the decimal
		float number = Float.parseFloat(input);
		int wholeNumber = (int) number;
		float decimals = number - wholeNumber;
		//Grabs the sign bit and converts the numbers to positive if needed
		String sign = getSign(wholeNumber);
		if (wholeNumber < 0)
			wholeNumber = -wholeNumber;
		if (decimals < 0)
			decimals = -decimals;
		//Gets the binary representation of the whole number
		String binaryNumber = getNumberString(wholeNumber);
		//Gets the max exponent needed for later
		int exponent = binaryNumber.length() - 1;
		//gets the binary representation for the decimal
		String binaryDecimal = getDecimalString32(decimals,exponent);
		//creates the middle 8 bits using the max exponent from number binary
		String middle = getMiddle8Bits(exponent);
		//Compiles the seperate elements together to give the 32 bit representation. 
		String result = sign + " " + middle + " " + binaryNumber.substring(1)
				+ binaryDecimal;
		return result;
	}

	private static String convert64Bit(String input) {
		//same as previous method except this is for 64 bit
		float number = Float.parseFloat(input);
		int wholeNumber = (int) number;
		float decimals = number - wholeNumber;
		String sign = getSign(wholeNumber);
		if (wholeNumber < 0)
			wholeNumber = -wholeNumber;
		if (decimals < 0)
			decimals = -decimals;
		String binaryNumber = getNumberString(wholeNumber);
		int exponent = binaryNumber.length() - 1;
		String binaryDecimal = getDecimalString64(decimals,exponent);
		String middle = getMiddle11Bits(exponent);
		String result = sign + " " + middle + " " + binaryNumber.substring(1)
				+ binaryDecimal;
		return result;
	}
	
	private static float convertBackToFloat32(String result) {
		boolean negative = result.charAt(0) == '1';
		result = result.substring(2);
		String middeString = result.substring(0, 8);
		int middleNum = Integer.parseInt(middeString, 2);
		int exponent = middleNum - 127;
		//adds the leading one back to the integer binary
		String endString = "1" + result.substring(9);
		String integerString = endString.substring(0, exponent + 1);
		String decimalBinaryString = endString.substring(exponent + 1);
		int resultNum = Integer.parseInt(integerString, 2);
		float decimal = getDecimal(decimalBinaryString);
		float finalResult = (float) resultNum + decimal;
		if (negative) {
			finalResult = (-finalResult);
		}
		return finalResult;
	}
	private static float convertBackToFloat64(String result) {
		//same as previous method except this is for 64 bit
		boolean negative = result.charAt(0) == '1';
		result = result.substring(2);
		String middeString = result.substring(0, 11);
		int middleNum = Integer.parseInt(middeString, 2);
		int exponent = middleNum - 1023;
		String endString = "1" + result.substring(12);
		String integerString = endString.substring(0, exponent + 1);
		String decimalBinaryString = endString.substring(exponent + 1);
		int resultNum = Integer.parseInt(integerString, 2);
		float decimal = getDecimal(decimalBinaryString);
		float finalResult = (float) resultNum + decimal;
		if (negative) {
			finalResult = (-finalResult);
		}
		return finalResult;
	}
	/*
	 * Converts a binary string to a float, 010 = 0*1/2 + 1*1/4 + 0*1/8
	 */
	private static float getDecimal(String decimalBinaryString) {
		int denominator = 2;
		float result = 0f;
		for (int i = 0; i < decimalBinaryString.length(); i++) {
			if (decimalBinaryString.charAt(i) == '1') {
				result += (float) (1.0 / denominator);
			}
			denominator = denominator * 2;
		}

		return result;
	}
	//Creates the middle bits based on the max exponent passed in
	private static String getMiddle8Bits(int exponent) {
		return Integer.toBinaryString(127 + exponent);
	}
	//same as above except for middle 11 bits (64 bit binary number)
	private static String getMiddle11Bits(int exponent) {
		return Integer.toBinaryString(1023 + exponent);
	}
	//converts a whole number to binary
	private static String getNumberString(int wholeNumber) {
		return Integer.toBinaryString(wholeNumber);
	}
	//returns the sign bit for the 32 bit String
	private static String getSign(int wholeNumber) {
		String binary = "";
		if (wholeNumber < 0) {
			binary += "1";
		} else {
			binary += "0";
		}
		return binary;
	}
	
	//Converts a decimal into binary ie: .75 = .5 + .25 + 0 = 110
	private static String getDecimalString32(float decimals, int exponent) {
		String binary = "";
		int denominator = 2;
		for (int i = 0; i < 23-exponent; i++) {
			if (decimals - (1.0 / denominator) >= 0) {
				binary += "1";
				decimals = (float) (decimals - (1.0 / denominator));
			} else {
				binary += "0";
			}
			denominator = denominator * 2;
		}
		return binary;
	}
	//same as previous method except this is for 64 bit
	private static String getDecimalString64(float decimals, int exponent) {
		String binary = "";
		int denominator = 2;
		for (int i = 0; i < 52-exponent; i++) {
			if (decimals - (1.0 / denominator) >= 0) {
				binary += "1";
				decimals = (float) (decimals - (1.0 / denominator));
			} else {
				binary += "0";
			}
			denominator = denominator * 2;
		}
		return binary;
	}
}
