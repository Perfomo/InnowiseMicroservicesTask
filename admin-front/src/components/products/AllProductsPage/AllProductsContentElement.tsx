
import { useNavigate } from 'react-router-dom';

interface CatalogElementProps {
  id: number;
  name: string;
  cost: number;
}

const AllProductsContentElement = (props: CatalogElementProps) => {
  const navigate = useNavigate();

  const onClick = () => {
    const name = props.name;
    navigate('/products/' + name + "/show", { state: { name: name } });
  };

  return (
    <div
      className="card text-bg-primary mb-3"
      onClick={onClick}
      style={{
        width: "200px",
        height: "140px",
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

export default AllProductsContentElement;
