using System;
#if FRAMEWORK11
	using System.Collections;
#else
	using System.Collections.Generic;
#endif

namespace org.drools.dotnet.util
{
#if FRAMEWORK11
	/// <summary>
	/// A read-only dictionary that wraps a java.util.Map class.
	/// </summary>
	internal class ReadOnlyDictionary : IDictionary
#else
	/// <summary>
	/// A read-only dictionary that wraps a java.util.Map class.
	/// </summary>
	/// <typeparam name="TKey">The type of the keys in the dictionary.</typeparam>
	/// <typeparam name="TValue">The type of the values in the dictionary.</typeparam>
	internal class ReadOnlyDictionary<TKey,TValue> : IDictionary<TKey, TValue>
#endif
	{
#if FRAMEWORK11
		private IDictionary _dictionary;
#else
		private IDictionary<TKey, TValue> _dictionary;
#endif
		private java.util.Map _map;

		public ReadOnlyDictionary(java.util.Map map)
		{
			_map = map;
#if FRAMEWORK11
			_dictionary = new Hashtable();
#else
			_dictionary = new Dictionary<TKey, TValue>();
#endif
			Refresh();
		}

#if FRAMEWORK11
		public bool ContainsKey(object key)
		{
			return ((Hashtable) _dictionary).ContainsKey(key);
		}
#else
		public bool ContainsKey(TKey key)
		{
			return _dictionary.ContainsKey(key);
		}
#endif

#if FRAMEWORK11
		public ICollection Keys
#else
		public ICollection<TKey> Keys
#endif
		{
			get { return _dictionary.Keys; }
		}

#if FRAMEWORK11
		public void Remove(object key)
#else
		public bool Remove(TKey key)
#endif
		{
			throw new InvalidOperationException("Cannot modify a read-only dictionary.");
		}

#if FRAMEWORK11
		public bool TryGetValue(object key, out object value)
		{
			bool hasValue = _dictionary.Contains(key);
			if (hasValue)
			{
				value = _dictionary[key];
			}
			else
			{
				value = null;
			}
			return hasValue;
		}
#else
		public bool TryGetValue(TKey key, out TValue value)
		{
			return _dictionary.TryGetValue(key, out value);
		}
#endif

#if FRAMEWORK11
		public ICollection Values
#else
		public ICollection<TValue> Values
#endif
		{
			get { return _dictionary.Values; }
		}

#if FRAMEWORK11
		public object this[object key]
#else
		public TValue this[TKey key]
#endif
		{
			get { return _dictionary[key]; }
			set { throw new InvalidOperationException("Cannot modify a read-only dictionary."); }
		}

#if FRAMEWORK11
		public void Add(object key, object value)
#else
		public void Add(TKey key, TValue value)
#endif
		{
			throw new InvalidOperationException("Cannot modify a read-only dictionary.");
		}

#if FRAMEWORK11
		public void Add(object item)
#else
		public void Add(KeyValuePair<TKey, TValue> item)
#endif
		{
			throw new InvalidOperationException("Cannot modify a read-only dictionary.");
		}

		public void Clear()
		{
			throw new InvalidOperationException("Cannot modify a read-only dictionary.");
		}

#if FRAMEWORK11
		public bool Contains(object item)
#else
		public bool Contains(KeyValuePair<TKey, TValue> item)
#endif
		{
			return _dictionary.Contains(item);
		}

#if FRAMEWORK11
		public void CopyTo(object[] array, int arrayIndex)
#else
		public void CopyTo(KeyValuePair<TKey, TValue>[] array, int arrayIndex)
#endif
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

#if FRAMEWORK11
#else
		public bool Remove(KeyValuePair<TKey, TValue> item)
		{
			throw new InvalidOperationException("Cannot modify a read-only dictionary.");
		}
#endif

#if FRAMEWORK11
		public IDictionaryEnumerator GetEnumerator()
#else
		public IEnumerator<KeyValuePair<TKey, TValue>> GetEnumerator()
#endif
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
#if FRAMEWORK11
				_dictionary.Add(key, _map.get(key));
#else
				_dictionary.Add((TKey)key, (TValue)_map.get(key));
#endif
			}
		}

#if FRAMEWORK11
		public bool IsFixedSize {get {return true;}}
		public object SyncRoot {get{return _dictionary.SyncRoot;}}
		public bool IsSynchronized  {get{return _dictionary.IsSynchronized ;}}

		public void CopyTo(Array array, int index)
		{
			_dictionary.CopyTo(array, index);
		}
#endif
	}
}
