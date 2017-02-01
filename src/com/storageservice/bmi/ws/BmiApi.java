package com.storageservice.bmi.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.WebResult;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

import com.storageservice.bmi.model.Bmi;
import com.storageservice.bmi.model.Person;


//service definition
@WebService
@SOAPBinding(style = Style.DOCUMENT, use=Use.LITERAL) 
public interface BmiApi {
	
    @WebMethod(operationName="calculateandSavebmi")
    @WebResult(name="bmi") 
    public Bmi CalculateAndSaveBmi(@WebParam(name="person") Person p,@WebParam(name="weight") double weight, @WebParam(name="height") double height);
    
    @WebMethod(operationName="getbmi")
    @WebResult(name="bmi") 
    public Bmi getBmi(@WebParam(name="idBmi") int id);
    
    @WebMethod(operationName="getPeriodBmiDifference")
    @WebResult(name="bmiDifference")  
    public double PeriodBmiDifference(@WebParam(name="startDate")String startDate,@WebParam(name="idPerson")int idPerson);
    
    @WebMethod(operationName="getStatusBmiDifference")
    @WebResult(name="statusBmiDifference")  
    public String StatusBmiDifference(@WebParam(name="startDate")String startDate,@WebParam(name="idPerson")int idPerson);
    
    @WebMethod(operationName="getBmiByIdPerson")
    @WebResult(name="bmi")  
    public Bmi getBmyByIdPerson(@WebParam(name="idPerson")int idPerson);
    
}