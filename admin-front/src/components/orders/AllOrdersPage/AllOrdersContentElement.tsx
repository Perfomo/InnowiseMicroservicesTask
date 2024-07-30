
import { useNavigate } from 'react-router-dom';

interface OrdersElementProps {
  id: number;
  username: string;
  products: any[];
  orderStatus: string;
  orderCost: number;
}

const AllOrdersContentElement = (props: OrdersElementProps) => {
  const navigate = useNavigate();

  const onClick = () => {
    const id = props.id;
    navigate('/orders/' + id + "/show", { state: { id: id } });
  };

  return (
    <div
      onClick={onClick}
      className={
        props.orderStatus === "OK"
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
        Order status: {props.orderStatus}
        <br/>
        {props.username}
      </div>
      <div className="card-body">
        {Object.entries(props.products).map(([name, amount], index) => (
          <p className="card-text" key={index}>
            {name} - {amount}
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

export default AllOrdersContentElement;
