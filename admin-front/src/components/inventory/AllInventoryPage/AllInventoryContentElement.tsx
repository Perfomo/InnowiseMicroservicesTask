
import { useNavigate } from 'react-router-dom';

interface InventoryElementProps {
  id: number;
  name: string;
  left: number;
  sold: number;
  cost: number;
}

const AllInventoryContentElement = (props: InventoryElementProps) => {
  const navigate = useNavigate();

  const onClick = () => {
    const name = props.name;
    navigate('/inventory/' + name + "/show", { state: { name: name } });
  };

  return (
    <div
      className="card text-bg-primary mb-3"
      onClick={onClick}
      style={{
        width: "200px",
        height: "180px",
        margin: "1%",
        textAlign: "center",
      }}
    >
      <div className="card-header" style={{ fontWeight: "700" }}>
        {props.id}
      </div>
      <div className="card-body">
        <p className="card-text">
        {props.name}
        <br/>
        Left: {props.left}
        <br/>

        Sold: {props.sold}
        </p>
        <p
          className="card-title"
          style={{ fontWeight: "700", textAlign: "center" }}
        >
          {props.cost}$
        </p>
      </div>
    </div>
  );
};

export default AllInventoryContentElement;
