package com.ess.api.request;

import com.ess.api.entities.Leave;

public class ApproveOrRejectRequest {
    private Leave leave;
    private AddNote note;

    private Long requestEmployeeId;

    public ApproveOrRejectRequest(Leave leave, AddNote note, Long requestEmployeeId) {
        this.leave = leave;
        this.note = note;
        this.requestEmployeeId = requestEmployeeId;
    }

    public Leave getLeave() {
        return leave;
    }

    public void setLeave(Leave leave) {
        this.leave = leave;
    }

    public AddNote getNote() {
        return note;
    }

    public void setNote(AddNote note) {
        this.note = note;
    }

    public Long getRequestEmployeeId() {
        return requestEmployeeId;
    }

    public void setRequestEmployeeId(Long requestEmployeeId) {
        this.requestEmployeeId = requestEmployeeId;
    }

    @Override
    public String toString() {
        return "ApproveOrRejectRequest{" +
                "leave=" + leave +
                ", note=" + note +
                ", requestEmployeeId=" + requestEmployeeId +
                '}';
    }
}
