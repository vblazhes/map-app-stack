package finki.ukim.mk.map_application.model;

public class PinDTO {
    int map_id;
    Pin pin;

    public PinDTO(){

    }

    public PinDTO(int map_id, Pin pin) {
        this.map_id = map_id;
        this.pin = pin;
    }

    public int getMap_id() {
        return map_id;
    }

    public Pin getPin() {
        return pin;
    }

    public void setMap_id(int map_id) {
        this.map_id = map_id;
    }

    public void setPin(Pin pin) {
        this.pin = pin;
    }
}
