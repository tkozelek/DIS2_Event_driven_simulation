package kozelek.entity.order;

import kozelek.entity.Workstation;
import kozelek.entity.worker.Worker;
import kozelek.entity.worker.WorkerGroup;

public class Order {
    private final int id;
    private final OrderType orderType;
    private OrderActivity orderActivity;
    private Workstation workstation;
    private Worker currentWorker;

    private double arrivalTime;
    private double finishTime;

    private double startCuttingTime;
    private double finishCuttingTime;

    private double startPaintingTime;
    private double finishPaintingTime;

    private double startDryingTime;
    private double finishDryingTime;

    private double startAssemblyTime;
    private double finishAssemblyTime;

    private double startFittingAssemblyTime;
    private double finishFittingAssemblyTime;

    public Order(int id, OrderType orderType) {
        this.id = id;
        this.orderType = orderType;
        this.orderActivity = OrderActivity.Empty;
    }

    public WorkerGroup getNextNeededGroup() {
        return switch (orderActivity) {
            case Empty -> WorkerGroup.GROUP_A;
            case Cut, Assembled -> WorkerGroup.GROUP_C;
            case Painted -> WorkerGroup.GROUP_B;
            default -> throw new IllegalStateException("Unexpected value: " + orderActivity);
        };
    }

    public int getId() {
        return id;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public Workstation getWorkstation() {
        return workstation;
    }

    public void setWorkstation(Workstation workstation) {
        this.workstation = workstation;
    }

    public OrderActivity getOrderActivity() {
        return orderActivity;
    }

    public void setOrderActivity(OrderActivity orderActivity) {
        this.orderActivity = orderActivity;
    }

    public Worker getCurrentWorker() {
        return currentWorker;
    }

    public void setCurrentWorker(Worker currentWorker) {
        this.currentWorker = currentWorker;
    }

    public double getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(double arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public double getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(double finishTime) {
        this.finishTime = finishTime;
    }

    public double getStartCuttingTime() {
        return startCuttingTime;
    }

    public void setStartCuttingTime(double startCuttingTime) {
        this.startCuttingTime = startCuttingTime;
    }

    public double getFinishCuttingTime() {
        return finishCuttingTime;
    }

    public void setFinishCuttingTime(double finishCuttingTime) {
        this.finishCuttingTime = finishCuttingTime;
    }

    public double getStartPaintingTime() {
        return startPaintingTime;
    }

    public void setStartPaintingTime(double startPaintingTime) {
        this.startPaintingTime = startPaintingTime;
    }

    public double getFinishPaintingTime() {
        return finishPaintingTime;
    }

    public void setFinishPaintingTime(double finishPaintingTime) {
        this.finishPaintingTime = finishPaintingTime;
    }

    public double getStartAssemblyTime() {
        return startAssemblyTime;
    }

    public void setStartAssemblyTime(double startAssemblyTime) {
        this.startAssemblyTime = startAssemblyTime;
    }

    public double getFinishAssemblyTime() {
        return finishAssemblyTime;
    }

    public void setFinishAssemblyTime(double finishAssemblyTime) {
        this.finishAssemblyTime = finishAssemblyTime;
    }

    public double getStartFittingAssemblyTime() {
        return startFittingAssemblyTime;
    }

    public double getStartDryingTime() {
        return startDryingTime;
    }

    public void setStartDryingTime(double startDryingTime) {
        this.startDryingTime = startDryingTime;
    }

    public double getFinishDryingTime() {
        return finishDryingTime;
    }

    public void setFinishDryingTime(double finishDryingTime) {
        this.finishDryingTime = finishDryingTime;
    }

    public void setStartFittingAssemblyTime(double startFittingAssemblyTime) {
        this.startFittingAssemblyTime = startFittingAssemblyTime;
    }

    public double getFinishFittingAssemblyTime() {
        return finishFittingAssemblyTime;
    }

    public void setFinishFittingAssemblyTime(double finishFittingAssemblyTime) {
        this.finishFittingAssemblyTime = finishFittingAssemblyTime;
    }

    @Override
    public String toString() {
        return String.format("order %d", id);
    }
}
