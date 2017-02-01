package com.storageservice.bmi.ws;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.jws.WebService;

import org.json.JSONObject;

import com.storageservice.bmi.model.Bmi;
import com.storageservice.bmi.model.BmiHistory;
import com.storageservice.bmi.model.Person;

//Service Implementation
@WebService(endpointInterface = "com.storageservice.bmi.ws.BmiApi", serviceName = "storageServiceBmi")
public class BmiApiImpl implements BmiApi {
	static String localurl = "https://localdbservice.herokuapp.com/localdbservice";

	@Override
	public Bmi CalculateAndSaveBmi(Person p, double weight, double height) {
		/*
		 * Person pverification = getPersonInformation(p.getIdPerson()); if
		 * (pverification == null) { int idPerson = registration(p); }
		 */
		System.out.println(p.getIdPerson());
		String url = "https://adapterservice.herokuapp.com/Bmi?weight=" + weight + "&height=" + height + "&sex="
				+ p.getGenre() + "&age=" + p.ageCalculator(p.getBirthdate());
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			con.setRequestMethod("GET");
			con.setRequestProperty("Accept", "application/json");
			int responseCode = con.getResponseCode();

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			System.out.println(response.toString());
			JSONObject jobj = new JSONObject(response.toString());
			Bmi bmi = new Bmi();
			bmi.setIdPerson(p.getIdPerson());
			bmi.setStatus(jobj.getString("status"));
			bmi.setValue(Double.parseDouble(jobj.getString("value")));
			bmi.setRisk(jobj.getString("risk"));
			bmi.setPrime(jobj.getString("prime"));

			Bmi OldBmi = Bmi.getBmiByIdPerson(p.getIdPerson());
			//System.out.println(OldBmi.getRisk());
			
			if (OldBmi == null) {
				Bmi.saveBmi(bmi);
			} else {
				Bmi.removeBmi(OldBmi);
				BmiHistory bmiHistory = new BmiHistory();
				bmiHistory.setIdPerson(OldBmi.getIdPerson());
				bmiHistory.setPrime(OldBmi.getPrime());
				bmiHistory.setRisk(OldBmi.getRisk());
				bmiHistory.setStatus(OldBmi.getStatus());
				bmiHistory.setValue(OldBmi.getValue());
				Date date = new Date();
				String sdate = new SimpleDateFormat("yyyy-MM-dd").format(date);
				bmiHistory.setDate(sdate);
				BmiHistory.saveBmiHistory(bmiHistory);
				Bmi.updateBmi(bmi);
			}
			return bmi;
		} catch (Exception e) {
			System.out.println("error sending bmi request to AdpaterService " + e);
			return null;
		}
	}

	@Override
	public Bmi getBmyByIdPerson(int idPerson) {
		try {
			return Bmi.getBmiByIdPerson(idPerson);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Bmi getBmi(int id) {
		Bmi bmi = Bmi.getBmiById(id);
		return bmi;
	}

	@Override
	public double PeriodBmiDifference(String startDate, int idPerson) {
		try {
			BmiHistory history = BmiHistory.getBmiHistoryByDate(startDate);
			Bmi actual = Bmi.getBmiByIdPerson(idPerson);
			return history.getValue() - actual.getValue();
		} catch (Exception e) {
			return 0;
		}

	}

	@Override
	public String StatusBmiDifference(String startDate, int idPerson) {
		try {
			BmiHistory history = BmiHistory.getBmiHistoryByDate(startDate);
			Bmi actual = Bmi.getBmiByIdPerson(idPerson);
			if (history.getStatus().equals(actual.getStatus())){
				return "Your bmi status is still the same and it is"+actual.getStatus()+" you can always improve yourself!"; 
			}else{
			return "Your status change from " + history.getStatus() + " to " + actual.getStatus();}
		} catch (Exception e) {
			return "";
		}
	}

}