package com.comino.msp.main.control.listener;

import com.comino.msp.model.segment.Status;

public interface IMSPModeChangedListener {

   public void update(Status oldStatus, Status newStatus);

}
