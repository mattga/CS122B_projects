package Types;

import Types.Movie;

import java.util.LinkedList;
import java.util.Iterator;

public class ShoppingCart {
	private LinkedList<Movie> movieCart;

	public ShoppingCart() {
		movieCart = new LinkedList<Movie>();
	}

	public void updateQuantity(int movieId, int quantity) {
		Iterator<Movie> iter = movieCart.descendingIterator();

		for(;iter.hasNext();) {
			Movie m = iter.next();
			if(m.id == movieId) {
				if(quantity == 0)
					iter.remove();
				else
					m.cartQuantity = quantity;
			}
		}
	}

	public void addMovie(Movie m) {
		Iterator<Movie> iter = movieCart.descendingIterator();

		for(;iter.hasNext();) {
			Movie m2 = iter.next();
			if(m.id == m2.id) {
				m2.cartQuantity++;
			} else {
				movieCart.add(m);
			}
		}	
	}

	public void removeMovie(int movieId) {
		Iterator<Movie> iter = movieCart.descendingIterator();

		for(;iter.hasNext();) {
			Movie m = iter.next();
			if(m.id == movieId) {
				iter.remove();
			}
		}	
	}

	public double getTotal() {
		double price = 0.00;

		for(Movie m : movieCart)
			price += (m.cartQuantity * 9.99);
		return price;
	}

	public Iterable<Movie> getCart() {
		return movieCart;
	}
}