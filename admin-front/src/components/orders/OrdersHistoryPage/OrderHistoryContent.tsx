interface OrdersElementProps {
    order: any;
}

const OrderHistoryContent = ({order}: OrdersElementProps) => {

  return (
    <div
      className={
        order.orderHistory.status === "OK"
          ? "card text-bg-primary mb-3"
          : "card text-bg-danger mb-3"
      }
      style={{
        width: "200px",
        minHeight: "200px",
        margin: "1%",
        textAlign: "center",
      }}
    >
      <div className="card-header" style={{ fontWeight: "700" }}>
        Order history Id: {order.id} <br/>
        Order Id: {order.orderHistory.orderId} <br/>
        Order status: {order.orderHistory.status} <br/>
        Order date: {order.time} <br/>
        Order username: {order.orderHistory.username}
      </div>
      <div className="card-body">
        {Object.entries(order.orderHistory.products).map(([name, amount], index) => (
          <p className="card-text" key={index}>
            {name} - {String(amount)}
          </p>
        ))}
      </div>
      <p
        className="card-title"
        style={{ fontWeight: "700", textAlign: "center" }}
      >
        Total cost: {order.orderHistory.cost}$
      </p>
    </div>
  );
};

export default OrderHistoryContent;
