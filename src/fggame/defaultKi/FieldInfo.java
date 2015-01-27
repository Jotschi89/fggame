/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fggame.defaultKi;

import java.io.Serializable;

/**
 * Kapselt alle Berechnungs Informationen für ein Feld.
 * @author Jotschi
 */
public class FieldInfo implements Serializable {
    
    private ParaInfo self_hor;
    private ParaInfo self_hor_mul;
    private ParaInfo self_ver;
    private ParaInfo self_ver_mul;
    private ParaInfo self_swtono;
    private ParaInfo self_swtono_mul;
    private ParaInfo self_nwtoso;
    private ParaInfo self_nwtoso_mul;
    private ParaInfo op_hor;
    private ParaInfo op_hor_mul;
    private ParaInfo op_ver;
    private ParaInfo op_ver_mul;
    private ParaInfo op_swtono;
    private ParaInfo op_swtono_mul;
    private ParaInfo op_nwtoso;
    private ParaInfo op_nwtoso_mul;
    private long total;

    public FieldInfo() {
    }

    public ParaInfo getOp_hor() {
        return op_hor;
    }

    public void setOp_hor(ParaInfo op_hor) {
        this.op_hor = op_hor;
    }

    public ParaInfo getOp_hor_mul() {
        return op_hor_mul;
    }

    public void setOp_hor_mul(ParaInfo op_hor_mul) {
        this.op_hor_mul = op_hor_mul;
    }

    public ParaInfo getOp_nwtoso() {
        return op_nwtoso;
    }

    public void setOp_nwtoso(ParaInfo op_nwtoso) {
        this.op_nwtoso = op_nwtoso;
    }

    public ParaInfo getOp_nwtoso_mul() {
        return op_nwtoso_mul;
    }

    public void setOp_nwtoso_mul(ParaInfo op_nwtoso_mul) {
        this.op_nwtoso_mul = op_nwtoso_mul;
    }

    public ParaInfo getOp_swtono() {
        return op_swtono;
    }

    public void setOp_swtono(ParaInfo op_swtono) {
        this.op_swtono = op_swtono;
    }

    public ParaInfo getOp_swtono_mul() {
        return op_swtono_mul;
    }

    public void setOp_swtono_mul(ParaInfo op_swtono_mul) {
        this.op_swtono_mul = op_swtono_mul;
    }

    public ParaInfo getOp_ver() {
        return op_ver;
    }

    public void setOp_ver(ParaInfo op_ver) {
        this.op_ver = op_ver;
    }

    public ParaInfo getOp_ver_mul() {
        return op_ver_mul;
    }

    public void setOp_ver_mul(ParaInfo op_ver_mul) {
        this.op_ver_mul = op_ver_mul;
    }

    public ParaInfo getSelf_hor() {
        return self_hor;
    }

    public void setSelf_hor(ParaInfo self_hor) {
        this.self_hor = self_hor;
    }

    public ParaInfo getSelf_hor_mul() {
        return self_hor_mul;
    }

    public void setSelf_hor_mul(ParaInfo self_hor_mul) {
        this.self_hor_mul = self_hor_mul;
    }

    public ParaInfo getSelf_nwtoso() {
        return self_nwtoso;
    }

    public void setSelf_nwtoso(ParaInfo self_nwtoso) {
        this.self_nwtoso = self_nwtoso;
    }

    public ParaInfo getSelf_nwtoso_mul() {
        return self_nwtoso_mul;
    }

    public void setSelf_nwtoso_mul(ParaInfo self_nwtoso_mul) {
        this.self_nwtoso_mul = self_nwtoso_mul;
    }

    public ParaInfo getSelf_swtono() {
        return self_swtono;
    }

    public void setSelf_swtono(ParaInfo self_swtono) {
        this.self_swtono = self_swtono;
    }

    public ParaInfo getSelf_swtono_mul() {
        return self_swtono_mul;
    }

    public void setSelf_swtono_mul(ParaInfo self_swtono_mul) {
        this.self_swtono_mul = self_swtono_mul;
    }

    public ParaInfo getSelf_ver() {
        return self_ver;
    }

    public void setSelf_ver(ParaInfo self_ver) {
        this.self_ver = self_ver;
    }

    public ParaInfo getSelf_ver_mul() {
        return self_ver_mul;
    }

    public void setSelf_ver_mul(ParaInfo self_ver_mul) {
        this.self_ver_mul = self_ver_mul;
    }

    /**
     * @return Gesamtpunktewert für das Feld
     */
    public long getTotal() {
        return total;
    }

    /** Setzt Gesamtpunktewert für das Feld.
     * @param total
     */
    public void setTotal(long total) {
        this.total = total;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELF:\n");
        sb.append("Horizontal: ").append(self_hor).append(" x ").append(self_hor_mul);
        sb.append(" = ").append(self_hor.getValue() * self_hor_mul.getValue()).append("\n");
        sb.append("Vertical: ").append(self_ver).append(" x ").append(self_ver_mul);
        sb.append(" = ").append(self_ver.getValue() * self_ver_mul.getValue()).append("\n");
        sb.append("SW to NO: ").append(self_swtono).append(" x ").append(self_swtono_mul);
        sb.append(" = ").append(self_swtono.getValue() * self_swtono_mul.getValue()).append("\n");
        sb.append("NW to SO: ").append(self_nwtoso).append(" x ").append(self_nwtoso_mul);
        sb.append(" = ").append(self_nwtoso.getValue() * self_nwtoso_mul.getValue()).append("\n");
        sb.append("OP:\n");
        sb.append("Horizontal: ").append(op_hor).append(" x ").append(op_hor_mul);
        sb.append(" = ").append(op_hor.getValue() * op_hor_mul.getValue()).append("\n");
        sb.append("Vertical: ").append(op_ver).append(" x ").append(op_ver_mul);
        sb.append(" = ").append(op_ver.getValue() * op_ver_mul.getValue()).append("\n");
        sb.append("SW to NO: ").append(op_swtono).append(" x ").append(op_swtono_mul);
        sb.append(" = ").append(op_swtono.getValue() * op_swtono_mul.getValue()).append("\n");
        sb.append("NW to SO: ").append(op_nwtoso).append(" x ").append(op_nwtoso_mul);
        sb.append(" = ").append(op_nwtoso.getValue() * op_nwtoso_mul.getValue()).append("\n");
        
        sb.append("\nRESULT = ").append(this.total);
        
        return sb.toString();
    }
}
