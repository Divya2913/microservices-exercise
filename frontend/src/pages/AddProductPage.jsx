import { useState } from "react";
import { createProduct } from "../services/productService";

function AddProductPage({onProductAdded}) {
  const [product, setProduct] = useState({
    name: "",
    price: "",
    stock: "",
  });

  const handleChange = (e) => {
    setProduct({
      ...product,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    await createProduct(product);
        onProductAdded();

    alert("Product Added!");

    setProduct({
      name: "",
      price: "",
      stock: "",
    });
  };

  return (
    <div>
      <h2>Add Product</h2>

      <form onSubmit={handleSubmit}>
        <input
          name="name"
          placeholder="Name"
          value={product.name}
          onChange={handleChange}
        />
        <br /><br />

        <input
          name="price"
          placeholder="Price"
          value={product.price}
          onChange={handleChange}
        />
        <br /><br />

        <input
          name="stock"
          placeholder="Stock"
          value={product.stock}
          onChange={handleChange}
        />
        <br /><br />

        <button type="submit">Save</button>
      </form>
    </div>
  );
}

export default AddProductPage;