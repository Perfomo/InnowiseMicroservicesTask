
interface CatalogElementProps {
    productName: string,
    productCost: number;
}

const CatalogElement = (props: CatalogElementProps) => {
  return (
    <div
      className="card text-bg-primary mb-3"
      style={{
        width: "200px",
        height: "200px",
        margin: "1%",
        textAlign: "center",
      }}
    >
      <div className="card-header" style={{ fontWeight: "700" }}>
        {props.productName}
      </div>
      <div className="card-body">
        <p className="card-text">
          Some quick example text to build on the card and make up the
          bulk of the card's content.
        </p>
        <p
          className="card-title"
          style={{ fontWeight: "700", textAlign: "center" }}
        >
          {props.productCost}$
        </p>
      </div>
    </div>
  );
};

export default CatalogElement;
