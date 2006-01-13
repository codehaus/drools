using System;
using System.Collections.Generic;

namespace org.drools.dotnet.util
{
	/// <summary>
	/// A read-only dictionary that wraps a java.util.Map class.
	/// </summary>
	/// <typeparam name="TKey">The type of the keys in the dictionary.</typeparam>
	/// <typeparam name="TValue">The type of the values in the dictionary.</typeparam>
	internal class ReadOnlyDictionary<TKey,TValue> : IDictionary<TKey, TValue>
	{
		private IDictionary<TKey, TValue> _dictionary;
		private java.util.Map _map;

		public ReadOnlyDictionary(java.util.Map map)
		{
			_map = map;
			_dictionary = new Dictionary<TKey, TValue>();
			Refresh();
		}

		public bool ContainsKey(TKey key)
		{
			return _dictionary.ContainsKey(key);
		}

		public ICollection<TKey> Keys
		{
			get { return _dictionary.Keys; }
		}

		public bool Remove(TKey key)
		{
			throw new InvalidOperationException("Cannot modify a read-only dictionary.");
		}

		public bool TryGetValue(TKey key, out TValue value)
		{
			return _dictionary.TryGetValue(key, out value);
		}

		public ICollection<TValue> Values
		{
			get { return _dictionary.Values; }
		}

		public TValue this[TKey key]
		{
			get { return _dictionary[key]; }
			set { throw new InvalidOperationException("Cannot modify a read-only dictionary."); }
		}

		public void Add(TKey key, TValue value)
		{
			throw new InvalidOperationException("Cannot modify a read-only dictionary.");
		}

		public void Add(KeyValuePair<TKey, TValue> item)
		{
			throw new InvalidOperationException("Cannot modify a read-only dictionary.");
		}

		public void Clear()
		{
			throw new InvalidOperationException("Cannot modify a read-only dictionary.");
		}

		public bool Contains(KeyValuePair<TKey, TValue> item)
		{
			return _dictionary.Contains(item);
		}

		public void CopyTo(KeyValuePair<TKey, TValue>[] array, int arrayIndex)
		{
			_dictionary.CopyTo(array, arrayIndex);
		}

		public int Count
		{
			get { return _dictionary.Count; }
		}

		public bool IsReadOnly
		{
			get { return true; }
		}

		public bool Remove(KeyValuePair<TKey, TValue> item)
		{
			throw new InvalidOperationException("Cannot modify a read-only dictionary.");
		}

		public IEnumerator<KeyValuePair<TKey, TValue>> GetEnumerator()
		{
			return _dictionary.GetEnumerator();
		}

		System.Collections.IEnumerator System.Collections.IEnumerable.GetEnumerator()
		{
			return _dictionary.GetEnumerator();
		}

		public java.util.Map Map
		{
			get { return _map; }
		}

		/// <summary>
		/// Refreshes the current dictionary from the existing map.  This is useful if the 
		/// underlying java Map has changed.
		/// </summary>
		public void Refresh()
		{
			_dictionary.Clear();
			java.util.Iterator i = _map.keySet().iterator();
			while (i.hasNext())
			{
				object key = i.next();
				_dictionary.Add((TKey)key, (TValue)_map.get(key));
			}
		}
	}
}
