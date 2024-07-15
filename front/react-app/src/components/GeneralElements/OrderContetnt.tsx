interface CatalogElementProps {
  products: any[];
  orderStatus: string;
  orderCost: number;
}

const OrderContent = (props: CatalogElementProps) => {
  return (
    <div
    className= {(props.orderStatus === "OK") ? "card text-bg-primary mb-3" : "card text-bg-danger mb-3"}
    style={{
        width: "200px",
        // height: "200px",
        minHeight: "200px",
        margin: "1%",
        textAlign: "center",
      }}
    >
      <div className="card-header" style={{ fontWeight: "700" }}>
        Order status: {props.orderStatus}
      </div>
      <div className="card-body">
        {Object.entries(props.products).map(([name, amount], index) => (
          <p className="card-text" key={index}>
            Product: {name} - amount: {amount}
          </p>
        ))}
      </div>
      <p
          className="card-title"
          style={{ fontWeight: "700", textAlign: "center" }}
        >
          Total cost: {props.orderCost}$
        </p>
    </div>
  );
};

export default OrderContent;
