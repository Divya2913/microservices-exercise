import { useEffect, useState } from "react";
import { getProducts } from "../services/productService";

function ProductListPage({reload}) {
  const [products, setProducts] = useState([]);

  useEffect(() => {
    loadProducts();
  }, [reload]);

  const loadProducts = async () => {
    try {
      const response = await getProducts();
      console.log("DATA: ", response.data);
      setProducts(response.data);
    } catch (error) {
      console.error("ERROR: ",error);
    }
  };

  return (
    <div>
      <h2>Products</h2>

      <table border="1" cellPadding="10">
        <thead>
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Price</th>
            <th>Stock</th>
          </tr>
        </thead>

        <tbody>
          {products.map((p) => (
            <tr key={p.id}>
              <td>{p.id}</td>
              <td>{p.name}</td>
              <td>{p.price}</td>
              <td>{p.stock}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default ProductListPage;