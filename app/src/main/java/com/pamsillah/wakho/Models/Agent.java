package com.pamsillah.wakho.Models;

/**
 * Created by .Net Developer on 24/2/2017.
 */

public class Agent {

    private String IDpic, BankName, AccNumber;

    public String getBankName() {
        return BankName;
    }

    public void setBankName(String bankName) {
        BankName = bankName;
    }

    public String getAccNumber() {
        return AccNumber;
    }

    public void setAccNumber(String accNumber) {
        AccNumber = accNumber;
    }

    public String getIDpic() {
        return IDpic;
    }

    public void setIDpic(String IDpic) {
        this.IDpic = IDpic;
    }

    public int getAgentId() {
        return AgentId;
    }

    public void setAgentId(int agentId) {
        AgentId = agentId;
    }

    public String getSubscriberId() {
        return SubscriberId;
    }

    public void setSubscriberId(String subscriberId) {
        SubscriberId = subscriberId;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getCompanyTel() {
        return CompanyTel;
    }

    public void setCompanyTel(String companyTel) {
        CompanyTel = companyTel;
    }


    public String getCompanyAdress() {
        return CompanyAdress;
    }

    public void setCompanyAdress(String companyAdress) {
        CompanyAdress = companyAdress;
    }

    public String getCompanyLogo() {
        return CompanyLogo;
    }

    public void setCompanyLogo(String companyLogo) {
        CompanyLogo = companyLogo;
    }

    public String getCompanyRegNumber() {
        return CompanyRegNumber;
    }

    public void setCompanyRegNumber(String companyRegNumber) {
        CompanyRegNumber = companyRegNumber;
    }

    public byte[] getIsFreelance() {
        return IsFreelance;
    }

    public void setIsFreelance(byte[] isFreelance) {
        IsFreelance = isFreelance;
    }

    public int AgentId;

    public String getIDPic() {
        return IDPic;
    }

    public void setIDPic(String IDPic) {
        this.IDPic = IDPic;
    }

    public String IDPic;

    public String getProofRes() {
        return ProofRes;
    }

    public void setProofRes(String proofRes) {
        ProofRes = proofRes;
    }

    public String ProofRes;
    public String SubscriberId;
    public String CompanyName;
    public String CompanyTel;
    public String CompanyAdress;
    public String CompanyLogo;
    public String CompanyRegNumber;
    public byte[] IsFreelance;
}
